/*
 *  Copyright 2010-2011 BigData.mx
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
package mx.bigdata.sat.cfdi.examples;

import java.math.BigDecimal;
import java.util.List;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import mx.bigdata.sat.cfdi.v33.schema.CMetodoPago;
import mx.bigdata.sat.cfdi.v33.schema.CMoneda;
import mx.bigdata.sat.cfdi.v33.schema.CPais;
import mx.bigdata.sat.cfdi.v33.schema.CTipoDeComprobante;
import mx.bigdata.sat.cfdi.v33.schema.CTipoFactor;
import mx.bigdata.sat.cfdi.v33.schema.CUsoCFDI;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.CfdiRelacionados;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.CfdiRelacionados.CfdiRelacionado;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.CuentaPredial;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.InformacionAduanera;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.v33.schema.ObjectFactory;

public final class ExampleCFDv33Factory {

    public static Comprobante createComprobante() throws Exception {
        ObjectFactory of = new ObjectFactory();
        Comprobante comp = of.createComprobante();
        comp.setVersion("3.3");
        comp.setSerie("F");
        comp.setFolio("12345");
        comp.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(2017, 07, 1, 0, 0, 0, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED));
//        comp.setSello();
        comp.setFormaPago("02");
        comp.setNoCertificado("20001000000200001428");
//        comp.setCertificado();
        comp.setCondicionesDePago("Crédito a 20 días");
        comp.setSubTotal(new BigDecimal("1550.00"));
        comp.setDescuento(new BigDecimal("100.00"));
        comp.setMoneda(CMoneda.MXN);
        comp.setTipoCambio(new BigDecimal("1"));
        comp.setTotal(new BigDecimal("1798"));
        comp.setTipoDeComprobante(CTipoDeComprobante.I);
        comp.setMetodoPago(CMetodoPago.PUE);
        comp.setLugarExpedicion("03240");
        comp.setConfirmacion("aB1cD");
        comp.setCfdiRelacionados(createCfdiRelacionados(of));
        comp.setEmisor(createEmisor(of));
        comp.setReceptor(createReceptor(of));
        comp.setConceptos(createConceptos(of));
        comp.setImpuestos(createImpuestos(of));
        return comp;
    }

    private static CfdiRelacionados createCfdiRelacionados(ObjectFactory of) {
        CfdiRelacionados cfdir = of.createComprobanteCfdiRelacionados();
        cfdir.setTipoRelacion("06");
        cfdir.getCfdiRelacionado().add(createCfdiRelacionado(of));
        return cfdir;
    }

    private static CfdiRelacionado createCfdiRelacionado(ObjectFactory of) {
        CfdiRelacionado cfdir = of.createComprobanteCfdiRelacionadosCfdiRelacionado();
        cfdir.setUUID("a0452045-89cb-4792-9cc0-153f21faab7f");
        return cfdir;
    }

    private static Emisor createEmisor(ObjectFactory of) {
        Emisor emisor = of.createComprobanteEmisor();
        emisor.setNombre("PHARMA PLUS SA DE CV");
        emisor.setRfc("PPL961114GZ1");
        emisor.setRegimenFiscal("601");
        return emisor;
    }

    private static Receptor createReceptor(ObjectFactory of) {
        Receptor receptor = of.createComprobanteReceptor();
        receptor.setRfc("PEPJ8001019Q8");
        receptor.setNombre("JUAN PEREZ PEREZ");
        receptor.setResidenciaFiscal(CPais.MEX);
        receptor.setNumRegIdTrib("ResidenteExtranjero1");
        receptor.setUsoCFDI(CUsoCFDI.G_01);
        return receptor;
    }

    private static Conceptos createConceptos(ObjectFactory of) {
        Conceptos cps = of.createComprobanteConceptos();
        List<Concepto> list = cps.getConcepto();
        Concepto c1 = of.createComprobanteConceptosConcepto();
        c1.setClaveProdServ("10101501");
        c1.setNoIdentificacion("GEN01");
        c1.setCantidad(new BigDecimal("1.0"));
        c1.setClaveUnidad("EA");
        c1.setUnidad("CAPSULAS");
        c1.setDescripcion("VIBRAMICINA 100MG 10");
        c1.setValorUnitario(new BigDecimal("775.00"));
        c1.setImporte(new BigDecimal("775.00"));
        c1.setImpuestos(createImpuestosConceptos(of));
        c1.getInformacionAduanera().add(createInformacionAduanera(of));
        c1.setCuentaPredial(createCuentaPredial(of));
        list.add(c1);
        Concepto c2 = of.createComprobanteConceptosConcepto();
        c2.setClaveProdServ("10101501");
        c2.setNoIdentificacion("GEN02");
        c2.setCantidad(new BigDecimal("1.0"));
        c2.setClaveUnidad("EA");
        c2.setUnidad("BOTELLA");
        c2.setDescripcion("CLORUTO 500M");
        c2.setImporte(new BigDecimal("775.00"));
        c2.setValorUnitario(new BigDecimal("775.00"));
        c2.setImpuestos(createImpuestosConceptos(of));
        c2.getInformacionAduanera().add(createInformacionAduanera(of));
        c2.setCuentaPredial(createCuentaPredial(of));
        list.add(c2);
        return cps;
    }

    private static Concepto.Impuestos createImpuestosConceptos(ObjectFactory of) {
        Concepto.Impuestos imp = of.createComprobanteConceptosConceptoImpuestos();
        Concepto.Impuestos.Traslados trs = of.createComprobanteConceptosConceptoImpuestosTraslados();
        Concepto.Impuestos.Traslados.Traslado tr = of.createComprobanteConceptosConceptoImpuestosTrasladosTraslado();
        tr.setBase(new BigDecimal("0.16"));
        tr.setImpuesto("002");
        tr.setTipoFactor(CTipoFactor.TASA);
        tr.setTasaOCuota("0.160000");
        tr.setImporte(new BigDecimal("124.00"));
        trs.getTraslado().add(tr);
        imp.setTraslados(trs);
        return imp;
    }

    private static InformacionAduanera createInformacionAduanera(ObjectFactory of) {
        InformacionAduanera ia = of.createComprobanteConceptosConceptoInformacionAduanera();
        ia.setNumeroPedimento("67  52  3924  8060097");
        return ia;
    }

    private static CuentaPredial createCuentaPredial(ObjectFactory of) {
        CuentaPredial cup = of.createComprobanteConceptosConceptoCuentaPredial();
        cup.setNumero("123456");
        return cup;
    }

    private static Impuestos createImpuestos(ObjectFactory of) {
        Impuestos imps = of.createComprobanteImpuestos();
        imps.setTotalImpuestosRetenidos(BigDecimal.ZERO);
        imps.setTotalImpuestosTrasladados(new BigDecimal("248.00"));
        imps.setTraslados(createTraslados(of));
        return imps;
    }

    private static Impuestos.Traslados createTraslados(ObjectFactory of) {
        Impuestos.Traslados its = of.createComprobanteImpuestosTraslados();
        Impuestos.Traslados.Traslado it = of.createComprobanteImpuestosTrasladosTraslado();
        it.setImpuesto("002");
        it.setTipoFactor(CTipoFactor.TASA);
        it.setTasaOCuota("0.160000");
        it.setImporte(new BigDecimal("248.00"));
        its.getTraslado().add(it);
        return its;
    }

}
