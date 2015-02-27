package mx.bigdata.sat.contabilidad_electronica;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
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

import mx.bigdata.sat.ce_catalogo_cuentas.schema.Catalogo;
import mx.bigdata.sat.common.NamespacePrefixMapperImpl;
import mx.bigdata.sat.common.URIResolverImpl;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class CuentasContablesv11 {

	private static final String XSLT = "/xslt/ce_catalogo_cuentas/CatalogoCuentas_1_1.xslt";

	private static final String[] XSD = new String[] {
		"/xsd/ce_catalogo_cuentas/CatalogoCuentas_1_1.xsd"
	};

	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	private static final String BASE_CONTEXT = "mx.bigdata.sat.ce_catalogo_cuentas.schema";

	private final static Joiner JOINER = Joiner.on(':');

	private final JAXBContext context ;

	public static final ImmutableMap<String, String> PREFIXES = 
			ImmutableMap.of("http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas", "catalogocuentas", 
							"http://www.w3.org/2001/XMLSchema-instance","xsi");
	
	// ,"http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas", "catalogocuentas"

	private final Map<String, String> localPrefixes = Maps.newHashMap(PREFIXES);

	/*************************************************************************************************************/
	
	
	
	/*************************************************************************************************************/
	
	private TransformerFactory tf;

//	final Comprobante document;
	final Catalogo document; 
	
	
	public CuentasContablesv11(InputStream in, String... contexts) throws Exception {
		this.context = getContext(contexts);
		this.document = load(in);
	}

	public CuentasContablesv11(Catalogo catalogo, String... contexts) throws Exception {
		this.context = getContext(contexts);
		this.document = copy(catalogo);
	}
	
	public void addNamespace(String uri, String prefix) {
		localPrefixes.put(uri, prefix);
	}

	public void setTransformerFactory(TransformerFactory tf) {
		this.tf = tf;   
		tf.setURIResolver(new URIResolverImpl());
	}
	
	public Catalogo sellarComprobante(PrivateKey key, X509Certificate cert) throws Exception {
		sellar(key, cert);
		return doGetCatalogo();
	}

	public void validar() throws Exception {
		validar(null);
	}

	
	public void sellar(PrivateKey key, X509Certificate cert) throws Exception {
		cert.checkValidity(); 
		String signature = getSignature(key);
		document.setSello(signature);
		byte[] bytes = cert.getEncoded();
		Base64 b64 = new Base64(-1);
		String certStr = b64.encodeToString(bytes);
		document.setCertificado(certStr);
		BigInteger bi = cert.getSerialNumber();
		document.setNoCertificado(new String(bi.toByteArray()));
	}

	public void validar(ErrorHandler handler) throws Exception {
		SchemaFactory sf =
				SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
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

	public void verificar() throws Exception {
		String certStr = document.getCertificado();
		Base64 b64 = new Base64();
		byte[] cbs = b64.decode(certStr);

		X509Certificate cert = KeyLoaderFactory.createInstance(KeyLoaderEnumeration.PUBLIC_KEY_LOADER,new ByteArrayInputStream(cbs)).getKey();

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

	
	public void guardar(OutputStream out) throws Exception {
		Marshaller m = context.createMarshaller();
		m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl(localPrefixes));
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas" + "http://www.sat.gob.mx/esquemas/ContabilidadE/1_1/CatalogoCuentas/CatalogoCuentas_1_1.xsd");
		byte[] xmlHeaderBytes = XML_HEADER.getBytes("UTF8");
		out.write(xmlHeaderBytes); 
		m.marshal(document, out);
	}

	public String getCadenaOriginal() throws Exception {
		byte[] bytes = getOriginalBytes();
		return new String(bytes, "UTF8");
	}

	public static Catalogo newComprobante(InputStream in) throws Exception {
		return load(in);
	}

//	public ComprobanteBase getComprobante() throws Exception {
//		return new CFDv32ComprobanteBase(doGetCatalogo());
//	}

	Catalogo doGetCatalogo() throws Exception {
		return copy(document);
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

	


	// FUNCIONES AUXILIARES	
	private static JAXBContext getContext(String[] contexts) throws Exception {
		List<String> ctx = Lists.asList(BASE_CONTEXT, contexts);
		return JAXBContext.newInstance(JOINER.join(ctx));
	}
	
	private static Catalogo load(InputStream source, String... contexts) 
			throws Exception {
		JAXBContext context = getContext(contexts);
		try {
			Unmarshaller u = context.createUnmarshaller();
			return (Catalogo) u.unmarshal(source);
		} finally {
			source.close();
		}
	}

	  // Defensive deep-copy
	  private Catalogo copy(Catalogo catalogo) throws Exception {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
	    DocumentBuilder db = dbf.newDocumentBuilder(); 
	    Document doc = db.newDocument();
	    Marshaller m = context.createMarshaller();
	    m.marshal(catalogo, doc);
	    Unmarshaller u = context.createUnmarshaller();
	    return (Catalogo) u.unmarshal(doc);
	  }
	  
}
