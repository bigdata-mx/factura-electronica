<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:cfdi="http://www.sat.gob.mx/cfd/3" xmlns:ecc="http://www.sat.gob.mx/ecc" xmlns:psgecfd="http://www.sat.gob.mx/psgecfd" xmlns:donat="http://www.sat.gob.mx/donat" xmlns:divisas="http://www.sat.gob.mx/divisas" xmlns:detallista="http://www.sat.gob.mx/detallista" xmlns:ecb="http://www.sat.gob.mx/ecb" xmlns:implocal="http://www.sat.gob.mx/implocal" xmlns:terceros="http://www.sat.gob.mx/terceros" xmlns:iedu="http://www.sat.gob.mx/iedu" xmlns:ventavehiculos="http://www.sat.gob.mx/ventavehiculos" xmlns:pfic="http://www.sat.gob.mx/pfic" xmlns:tpe="http://www.sat.gob.mx/TuristaPasajeroExtranjero" xmlns:leyendasFisc="http://www.sat.gob.mx/leyendasFiscales" xmlns:spei="http://www.sat.gob.mx/spei" xmlns:nomina="http://www.sat.gob.mx/nomina" xmlns:registrofiscal="http://www.sat.gob.mx/registrofiscal" xmlns:pagoenespecie="http://www.sat.gob.mx/pagoenespecie" xmlns:consumodecombustibles="http://www.sat.gob.mx/consumodecombustibles" xmlns:valesdedespensa="http://www.sat.gob.mx/valesdedespensa" xmlns:aerolineas="http://www.sat.gob.mx/aerolineas" xmlns:notariospublicos="http://www.sat.gob.mx/notariospublicos" xmlns:vehiculousado="http://www.sat.gob.mx/vehiculousado" xmlns:servicioparcial="http://www.sat.gob.mx/servicioparcialconstruccion" xmlns:destruccion="http://www.sat.gob.mx/certificadodestruccion" xmlns:decreto="http://www.sat.gob.mx/renovacionysustitucionvehiculos" xmlns:obrasarte="http://www.sat.gob.mx/arteantiguedades" xmlns:aieps="http://www.sat.gob.mx/acreditamiento" xmlns:ecc11="http://www.sat.gob.mx/EstadoDeCuentaCombustible" xmlns:cce="http://www.sat.gob.mx/ComercioExterior" xmlns:ine="http://www.sat.gob.mx/ine" xmlns:nomina12="http://www.sat.gob.mx/nomina12" xmlns:pago10="http://www.sat.gob.mx/Pagos" xmlns:cce11="http://www.sat.gob.mx/ComercioExterior11">
    <!-- Con el siguiente método se establece que la salida deberá ser en texto -->
    <xsl:output method="text" version="1.0" encoding="UTF-8" indent="no"/>
    <!--
            En esta sección se define la inclusión de las plantillas de utilerías para colapsar espacios
    -->
    <xsl:include href="/xslt/cfd/2/cadenaoriginal_2_0/utilerias.xslt"/>
    <!-- 
            En esta sección se define la inclusión de las demás plantillas de transformación para 
            la generación de las cadenas originales de los complementos fiscales 
    -->
    <xsl:include href="/xslt/cfd/ecc/ecc.xslt"/>
    <xsl:include href="/xslt/cfd/ecc/ecc11.xslt"/>
    <xsl:include href="/xslt/cfd/donat/donat11.xslt"/>
    <xsl:include href="/xslt/cfd/divisas/divisas.xslt"/>
    <xsl:include href="/xslt/cfd/implocal/implocal.xslt"/>
    <xsl:include href="/xslt/cfd/leyendasFiscales/leyendasFisc.xslt"/>
    <xsl:include href="/xslt/cfd/pfic/pfic.xslt"/>
    <xsl:include href="/xslt/cfd/TuristaPasajeroExtranjero/TuristaPasajeroExtranjero.xslt"/>
    <xsl:include href="/xslt/cfd/spei/spei.xslt"/>
    <xsl:include href="/xslt/cfd/detallista/detallista.xslt"/>
    <xsl:include href="/xslt/cfd/cfdiregistrofiscal/cfdiregistrofiscal.xslt"/>
    <xsl:include href="/xslt/cfd/nomina/nomina12.xslt"/>
    <xsl:include href="/xslt/cfd/pagoenespecie/pagoenespecie.xslt"/>
    <xsl:include href="/xslt/cfd/valesdedespensa/valesdedespensa.xslt"/>
    <xsl:include href="/xslt/cfd/consumodecombustibles/consumodecombustibles.xslt"/>
    <xsl:include href="/xslt/cfd/aerolineas/aerolineas.xslt"/>
    <xsl:include href="/xslt/cfd/notariospublicos/notariospublicos.xslt"/>
    <xsl:include href="/xslt/cfd/vehiculousado/vehiculousado.xslt"/>
    <xsl:include href="/xslt/cfd/servicioparcialconstruccion/servicioparcialconstruccion.xslt"/>
    <xsl:include href="/xslt/cfd/renovacionysustitucionvehiculos/renovacionysustitucionvehiculos.xslt"/>
    <xsl:include href="/xslt/cfd/certificadodedestruccion/certificadodedestruccion.xslt"/>
    <xsl:include href="/xslt/cfd/obrasarteantiguedades/obrasarteantiguedades.xslt"/>
    <xsl:include href="/xslt/cfd/ine/INE11.xslt"/>
    <xsl:include href="/xslt/cfd/ComercioExterior/ComercioExterior10.xslt"/>
    <xsl:include href="/xslt/cfd/ComercioExterior/ComercioExterior11.xslt"/>
    <xsl:include href="/xslt/cfd/iedu/iedu.xslt"/>
    <xsl:include href="/xslt/cfd/ventavehiculos/ventavehiculos11.xslt"/>
    <xsl:include href="/xslt/cfd/terceros/terceros.xslt"/>
    <xsl:include href="/xslt/cfd/Pagos/Pagos10.xslt"/>
    <xsl:include href="/xslt/cfd/psgecfd/psgecfd.xslt"/>
    <xsl:include href="/xslt/cfd/ecb/ecb.xslt"/>
	
    <!-- Aquí iniciamos el procesamiento de la cadena original con su | inicial y el terminador || -->
    <xsl:template match="/">|<xsl:apply-templates select="/cfdi:Comprobante"/>||</xsl:template>
    <!--  Aquí iniciamos el procesamiento de los datos incluidos en el comprobante -->
    <xsl:template match="cfdi:Comprobante">
        <!-- Iniciamos el tratamiento de los atributos de comprobante -->
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Version"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Serie"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Folio"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Fecha"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@FormaPago"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@NoCertificado"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@CondicionesDePago"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@SubTotal"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Descuento"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Moneda"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@TipoCambio"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Total"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@TipoDeComprobante"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@MetodoPago"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@LugarExpedicion"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Confirmacion"/>
        </xsl:call-template>
        <!--
                Llamadas para procesar al los sub nodos del comprobante
        -->
        <xsl:apply-templates select="./cfdi:CfdiRelacionados"/>
        <xsl:apply-templates select="./cfdi:Emisor"/>
        <xsl:apply-templates select="./cfdi:Receptor"/>
        <xsl:apply-templates select="./cfdi:Conceptos"/>
        <xsl:apply-templates select="./cfdi:Impuestos"/>
        <xsl:for-each select="./cfdi:Complemento">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <!-- Manejador de nodos tipo CFDIRelacionados -->
    <xsl:template match="cfdi:CfdiRelacionados">
        <!-- Iniciamos el tratamiento de los atributos del CFDIRelacionados -->
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@TipoRelacion"/>
        </xsl:call-template>
        <xsl:for-each select="./cfdi:CfdiRelacionado">
            <xsl:call-template name="Opcional">
                <xsl:with-param name="valor" select="./@UUID"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <!-- Manejador de nodos tipo Emisor -->
    <xsl:template match="cfdi:Emisor">
        <!-- Iniciamos el tratamiento de los atributos del Emisor -->
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Rfc"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Nombre"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@RegimenFiscal"/>
        </xsl:call-template>
    </xsl:template>

    <!-- Manejador de nodos tipo Receptor -->
    <xsl:template match="cfdi:Receptor">
        <!-- Iniciamos el tratamiento de los atributos del Receptor -->
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Rfc"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Nombre"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@ResidenciaFiscal"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@NumRegIdTrib"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@UsoCFDI"/>
        </xsl:call-template>

    </xsl:template>

    <!-- Manejador de nodos tipo Conceptos -->
    <xsl:template match="cfdi:Conceptos">
        <!-- Llamada para procesar los distintos nodos tipo Concepto -->
        <xsl:for-each select="./cfdi:Concepto">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <!--Manejador de nodos tipo Concepto-->
    <xsl:template match="cfdi:Concepto">
        <!-- Iniciamos el tratamiento de los atributos del Concepto -->
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@ClaveProdServ"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@NoIdentificacion"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Cantidad"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@ClaveUnidad"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Unidad"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Descripcion"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@ValorUnitario"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Importe"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Descuento"/>
        </xsl:call-template>

        <!-- Manejo de sub nodos de información Traslado de Conceptos:Concepto:Impuestos:Traslados-->
        <xsl:for-each select="./cfdi:Conceptos/cfdi:Concepto/cfdi:Impuestos/cfdi:Traslados/cfdi:Traslado">
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Base"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Impuesto"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@TipoFactor"/>
            </xsl:call-template>
            <xsl:call-template name="Opcional">
                <xsl:with-param name="valor" select="./@TasaOCuota"/>
            </xsl:call-template>
            <xsl:call-template name="Opcional">
                <xsl:with-param name="valor" select="./@Importe"/>
            </xsl:call-template>
        </xsl:for-each>

        <!-- Manejo de sub nodos de Retencion por cada una de los Conceptos:Concepto:Impuestos:Retenciones-->
        <xsl:for-each select="./cfdi:Conceptos/cfdi:Concepto/cfdi:Impuestos/cfdi:Retenciones/cfdi:Retencion">
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Base"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Impuesto"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@TipoFactor"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@TasaOCuota"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Importe"/>
            </xsl:call-template>
        </xsl:for-each>

        <!-- Manejo de los distintos sub nodos de información aduanera de forma indistinta a su grado de dependencia -->
        <xsl:for-each select=".//cfdi:InformacionAduanera">
            <xsl:apply-templates select="."/>
        </xsl:for-each>

        <!-- Llamada al manejador de nodos de CuentaPredial en caso de existir -->
        <xsl:if test="./cfdi:CuentaPredial">
            <xsl:apply-templates select="./cfdi:CuentaPredial"/>
        </xsl:if>

        <!-- Llamada al manejador de nodos de ComplementoConcepto en caso de existir -->
        <xsl:if test="./cfdi:ComplementoConcepto">
            <xsl:apply-templates select="./cfdi:ComplementoConcepto"/>
        </xsl:if>

        <!-- Llamada al manejador de nodos de Parte en caso de existir -->
        <xsl:for-each select=".//cfdi:Parte">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <!-- Manejador de nodos tipo Información Aduanera -->
    <xsl:template match="cfdi:InformacionAduanera">
        <!-- Manejo de los atributos de la información aduanera -->
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@NumeroPedimento"/>
        </xsl:call-template>
    </xsl:template>

    <!-- Manejador de nodos tipo Información CuentaPredial -->
    <xsl:template match="cfdi:CuentaPredial">
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Numero"/>
        </xsl:call-template>
    </xsl:template>

    <!-- Manejador de nodos tipo ComplementoConcepto -->
    <xsl:template match="cfdi:ComplementoConcepto">
        <xsl:for-each select="./*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <!-- Manejador de nodos tipo Parte -->
    <xsl:template match="cfdi:Parte">
        <!-- Iniciamos el tratamiento de los atributos de Parte-->
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@ClaveProdServ"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@NoIdentificacion"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Cantidad"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Unidad"/>
        </xsl:call-template>
        <xsl:call-template name="Requerido">
            <xsl:with-param name="valor" select="./@Descripcion"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@ValorUnitario"/>
        </xsl:call-template>
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@Importe"/>
        </xsl:call-template>

        <!-- Manejador de nodos tipo InformacionAduanera-->
        <xsl:for-each select=".//cfdi:InformacionAduanera">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <!-- Manejador de nodos tipo Complemento -->
    <xsl:template match="cfdi:Complemento">
        <xsl:for-each select="./*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <!-- Manejador de nodos tipo Domicilio fiscal -->
    <xsl:template match="cfdi:Impuestos">
        <!-- Manejo de sub nodos de Retencion por cada una de los Impuestos:Retenciones-->
        <xsl:for-each select="./cfdi:Impuestos/cfdi:Retenciones/cfdi:Retencion">
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Impuesto"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Importe"/>
            </xsl:call-template>
        </xsl:for-each>
        <!-- Iniciamos el tratamiento de los atributos de TotalImpuestosRetenidos-->
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@TotalImpuestosRetenidos"/>
        </xsl:call-template>
        <!-- Manejo de sub nodos de información Traslado de Impuestos:Traslados-->
        <xsl:for-each select="./cfdi:Impuestos/cfdi:Traslados/cfdi:Traslado">
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Impuesto"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@TipoFactor"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@TasaOCuota"/>
            </xsl:call-template>
            <xsl:call-template name="Requerido">
                <xsl:with-param name="valor" select="./@Importe"/>
            </xsl:call-template>
        </xsl:for-each>
        <!-- Iniciamos el tratamiento de los atributos de TotalImpuestosTrasladados-->
        <xsl:call-template name="Opcional">
            <xsl:with-param name="valor" select="./@TotalImpuestosTrasladados"/>
        </xsl:call-template>
    </xsl:template>
</xsl:stylesheet>