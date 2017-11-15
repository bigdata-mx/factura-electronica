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
package mx.bigdata.sat.cfd.examples;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import mx.bigdata.sat.cfd.v22.schema.Comprobante;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Emisor.RegimenFiscal;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Impuestos;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Impuestos.Traslados;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Impuestos.Traslados.Traslado;
import mx.bigdata.sat.cfd.v22.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfd.v22.schema.ObjectFactory;
import mx.bigdata.sat.cfd.v22.schema.TUbicacion;
import mx.bigdata.sat.cfd.v22.schema.TUbicacionFiscal;

public final class ExampleCFDv22Factory {

    public static Comprobante createComprobante() throws Exception {
        return createComprobante(2012);
    }

    public static Comprobante createComprobante(int year) throws Exception {
        ObjectFactory of = new ObjectFactory();
        Comprobante comp = of.createComprobante();
        comp.setVersion("2.2");
        Date date = new GregorianCalendar(year, 4, 3, 14, 11, 36).getTime();
        comp.setFecha(date);
        comp.setSerie("ABCD");
        comp.setFolio("2");
        comp.setNoAprobacion(new BigInteger("49"));
        comp.setAnoAprobacion(new BigInteger("2008"));
        comp.setFormaDePago("UNA SOLA EXHIBICI\u00D3N");
        comp.setSubTotal(new BigDecimal("2000.00"));
        comp.setTotal(new BigDecimal("2320.00"));
        comp.setDescuento(new BigDecimal("0.00"));
        comp.setTipoDeComprobante("ingreso");
        comp.setMetodoDePago("efectivo");
        comp.setLugarExpedicion("Mexico");
        comp.setEmisor(createEmisor(of));
        comp.setReceptor(createReceptor(of));
        comp.setConceptos(createConceptos(of));
        comp.setImpuestos(createImpuestos(of));
        return comp;
    }

    private static Emisor createEmisor(ObjectFactory of) {
        Emisor emisor = of.createComprobanteEmisor();
        emisor.setNombre("CONTRIBUYENTE PRUEBASEIS PATERNOSEIS MATERNOSEIS");
        emisor.setRfc("PAMC660606ER9");
        TUbicacionFiscal uf = of.createTUbicacionFiscal();
        uf.setCalle("PRUEBA SEIS");
        uf.setCodigoPostal("72000");
        uf.setColonia("PUEBLA CENTRO");
        uf.setLocalidad("PUEBLA");
        uf.setMunicipio("PUEBLA");
        uf.setEstado("PUEBLA");
        uf.setNoExterior("6");
        uf.setNoInterior("6");
        uf.setPais("M\u00C9XICO");
        TUbicacion u = of.createTUbicacion();
        u.setCalle("PRUEBA SEIS");
        u.setCodigoPostal("72000");
        u.setColonia("PUEBLA CENTRO");
        u.setLocalidad("PUEBLA");
        u.setMunicipio("PUEBLA");
        u.setEstado("PUEBLA");
        u.setNoExterior("6");
        u.setNoInterior("6");
        u.setPais("M\u00C9XICO");
        emisor.setExpedidoEn(u);
        RegimenFiscal rf = of.createComprobanteEmisorRegimenFiscal();
        rf.setRegimen("simplificado");
        emisor.getRegimenFiscal().add(rf);
        return emisor;
    }

    private static Receptor createReceptor(ObjectFactory of) {
        Receptor receptor = of.createComprobanteReceptor();
        receptor.setNombre("ROSA MAR\u00CDA CALDER\u00D3N UIRIEGAS");
        receptor.setRfc("CAUR390312S87");
        TUbicacion uf = of.createTUbicacion();
        uf.setCalle("TOPOCHICO");
        uf.setCodigoPostal("95465");
        uf.setColonia("JARDINES DEL VALLE");
        uf.setEstado("NUEVO LEON");
        uf.setNoExterior("52");
        uf.setPais("M\u00E9xico");
        receptor.setDomicilio(uf);
        return receptor;
    }

    private static Conceptos createConceptos(ObjectFactory of) {
        Conceptos cps = of.createComprobanteConceptos();
        List<Concepto> list = cps.getConcepto();
        Concepto c1 = of.createComprobanteConceptosConcepto();
        c1.setUnidad("Servicio");
        c1.setNoIdentificacion("01");
        c1.setImporte(new BigDecimal("2000.00"));
        c1.setCantidad(new BigDecimal("1.00"));
        c1.setDescripcion("Asesoria Fiscal y administrativa");
        c1.setValorUnitario(new BigDecimal("2000.00"));
        list.add(c1);
        return cps;
    }

    private static Impuestos createImpuestos(ObjectFactory of) {
        Impuestos imps = of.createComprobanteImpuestos();
        imps.setTotalImpuestosTrasladados(new BigDecimal("320.00"));
        Traslados trs = of.createComprobanteImpuestosTraslados();
        List<Traslado> list = trs.getTraslado();
        Traslado t1 = of.createComprobanteImpuestosTrasladosTraslado();
        t1.setImporte(new BigDecimal("320.00"));
        t1.setImpuesto("IVA");
        t1.setTasa(new BigDecimal("16.00"));
        list.add(t1);
        imps.setTraslados(trs);
        return imps;
    }

}
