/*
 *  Copyright 2010 BigData.mx
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package mx.bigdata.sat.cfdi;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import mx.bigdata.sat.cfdi.schema.Comprobante;
import mx.bigdata.sat.cfdi.schema.ObjectFactory;
import mx.bigdata.sat.common.ComprobanteBase;
import mx.bigdata.sat.common.NamespacePrefixMapperImpl;
import mx.bigdata.sat.common.URIResolverImpl;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

public final class CFDv3 implements CFDI {

    private static final String XSLT = "/xslt/cadenaoriginal_3_0.xslt";

    private static final String[] XSD = new String[]{
        "/xsd/v3/cfdv3.xsd",
        "/xsd/v3/TimbreFiscalDigital.xsd",
        "/xsd/common/ecc/v10/ecc.xsd",
        "/xsd/common/divisas/divisas.xsd",
        "/xsd/common/implocal/implocal.xsd",
        "/xsd/common/detallista/detallista.xsd",
        "/xsd/common/iedu/iedu.xsd",
        "/xsd/common/ecb/ecb.xsd",
        "/xsd/common/psgcfdsp/psgcfdsp.xsd",
        "/xsd/common/psgecfd/psgecfd.xsd"
    };

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static final String BASE_CONTEXT = "mx.bigdata.sat.cfdi.schema";

    private final static Joiner JOINER = Joiner.on(':');

    private final JAXBContext context;

    public static final ImmutableMap<String, String> PREFIXES = ImmutableMap.of("http://www.w3.org/2001/XMLSchema-instance", "xsi", "http://www.sat.gob.mx/cfd/3", "cfdi", "http://www.sat.gob.mx/TimbreFiscalDigital", "tfd");

    private final Map<String, String> localPrefixes = Maps.newHashMap(PREFIXES);

    private TransformerFactory tf;

    final Comprobante document;

    public CFDv3(InputStream in, String... contexts) throws Exception {
        this.context = getContext(contexts);
        this.document = load(in);
    }

    public CFDv3(Comprobante comprobante, String... contexts) throws Exception {
        this.context = getContext(contexts);
        this.document = copy(comprobante);
    }

    public void addNamespace(String uri, String prefix) {
        localPrefixes.put(uri, prefix);
    }

    @Override
    public void setTransformerFactory(TransformerFactory tf) {
        this.tf = tf;
        tf.setURIResolver(new URIResolverImpl());
    }

    @Override
    public void sellar(PrivateKey key, X509Certificate cert) throws Exception {
        String nc = new String(cert.getSerialNumber().toByteArray());
        if (!nc.equals("20001000000200001428")) {
            cert.checkValidity();
        }
        String signature = getSignature(key);
        document.setSello(signature);
        byte[] bytes = cert.getEncoded();
        Base64 b64 = new Base64(-1);
        String certStr = b64.encodeToString(bytes);
        document.setCertificado(certStr);
        document.setNoCertificado(nc);
    }

    public Comprobante sellarComprobante(PrivateKey key, X509Certificate cert) throws Exception {
        sellar(key, cert);
        return doGetComprobante();
    }

    @Override
    public void validar() throws Exception {
        validar(null);
    }

    @Override
    public void validar(ErrorHandler handler) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source[] schemas = new Source[XSD.length];
        for (int i = 0; i < XSD.length; i++) {
            schemas[i] = new StreamSource(getClass().getResourceAsStream(XSD[i]));
        }
        Schema schema = sf.newSchema(schemas);
        Validator validator = schema.newValidator();
        if (handler != null) {
            validator.setErrorHandler(handler);
        }
        validator.validate(new JAXBSource(context, document));
    }

    @Override
    public void verificar() throws Exception {
        String certStr = document.getCertificado();
        Base64 b64 = new Base64();
        byte[] cbs = b64.decode(certStr);

        X509Certificate cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new ByteArrayInputStream(cbs)
        ).getKey();

        String sigStr = document.getSello();
        byte[] signature = b64.decode(sigStr);
        byte[] bytes = getOriginalBytes();
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(cert);
        sig.update(bytes);
        boolean bool = sig.verify(signature);
        if (!bool) {
            throw new Exception("Invalid signature");
        }
    }

    @Override
    public void guardar(OutputStream out) throws Exception {
        Marshaller m = context.createMarshaller();
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl(localPrefixes));
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv3.xsd");
        byte[] xmlHeaderBytes = XML_HEADER.getBytes("UTF8");
        out.write(xmlHeaderBytes);
        m.marshal(document, out);
    }

    @Override
    public String getCadenaOriginal() throws Exception {
        byte[] bytes = getOriginalBytes();
        return new String(bytes, "UTF8");
    }

    public static Comprobante newComprobante(InputStream in) throws Exception {
        return load(in);
    }

    byte[] getOriginalBytes() throws Exception {
        JAXBSource in = new JAXBSource(context, document);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Result out = new StreamResult(baos);
        TransformerFactory factory = tf;
        if (factory == null) {
            factory = TransformerFactory.newInstance();
            factory.setURIResolver(new URIResolverImpl());
        }
        Transformer transformer = factory.newTransformer(new StreamSource(getClass().getResourceAsStream(XSLT)));
        transformer.transform(in, out);
        return baos.toByteArray();
    }

    String getSignature(PrivateKey key) throws Exception {
        byte[] bytes = getOriginalBytes();
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(bytes);
        byte[] signed = sig.sign();
        Base64 b64 = new Base64(-1);
        return b64.encodeToString(signed);
    }

    @Override
    public ComprobanteBase getComprobante() throws Exception {
        return new CFDv3ComprobanteBase(doGetComprobante());
    }

    Comprobante doGetComprobante() throws Exception {
        return copy(document);
    }

    // Defensive deep-copy
    private Comprobante copy(Comprobante comprobante) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Marshaller m = context.createMarshaller();
        m.marshal(comprobante, doc);
        Unmarshaller u = context.createUnmarshaller();
        return (Comprobante) u.unmarshal(doc);
    }

    public static final class CFDv3ComprobanteBase implements ComprobanteBase {

        private final Comprobante document;

        public CFDv3ComprobanteBase(Comprobante document) {
            this.document = document;
        }

        @Override
        public boolean hasComplemento() {
            return document.getComplemento() != null;
        }

        @Override
        public List<Object> getComplementoGetAny() {
            return document.getComplemento().getAny();
        }

        @Override
        public String getSello() {
            return document.getSello();
        }

        @Override
        public void setComplemento(Element element) {
            ObjectFactory of = new ObjectFactory();
            Comprobante.Complemento comp = of.createComprobanteComplemento();
            List<Object> list = comp.getAny();
            list.add(element);
            document.setComplemento(comp);
        }

        @Override
        public Object getComprobante() {
            return document;
        }

    }

    private static JAXBContext getContext(String[] contexts) throws Exception {
        List<String> ctx = Lists.asList(BASE_CONTEXT, contexts);
        return JAXBContext.newInstance(JOINER.join(ctx));
    }

    private static Comprobante load(InputStream source, String... contexts) throws Exception {
        JAXBContext context = getContext(contexts);
        try {
            Unmarshaller u = context.createUnmarshaller();
            return (Comprobante) u.unmarshal(source);
        } finally {
            source.close();
        }
    }

    static void dump(String title, byte[] bytes, PrintStream out) {
        out.printf("%s: ", title);
        for (byte b : bytes) {
            out.printf("%02x ", b & 0xff);
        }
        out.println();
    }

}
