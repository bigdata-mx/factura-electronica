factura-electronica
====================

Librería de componentes Java para el desarrollo de aplicaciones de Factura Electrónica (CFDI)

[![Build Status](https://secure.travis-ci.org/bigdata-mx/factura-electronica.png)](http://travis-ci.org/bigdata-mx/factura-electronica)


## Librería de componentes

La librería presenta una interfaz muy simple centrada en el Comprobante Fiscal
Digital (CFD), la clase principal es `CFDv33` que tiene la lógica 
correspondiente a la versión 3.3 del CFDi.

Cada uno de estos elementos tiene la funcionalidad necesaria para: validar, firmar, verificar y serializar CFDs.

### Comprobante Fiscal Digital  (CFDv33):

```java
    CFDv33 cfd = new CFDv33(new FileInputStream(file)); // Crea un CFD a partir de un InputStream
    Key key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile),  password);
    Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile));
    Comprobante sellado = cfd.sellarComprobante(key, cert); // Firma el CFD y obtiene un Comprobante sellado
    cfd.validar(); // Valida el XML, que todos los elementos estén presentes
    cfd.verificar(); // Verifica un CFD ya firmado
    cfd.guardar(System.out); // Serializa el CFD a un OutputStream
```

### Timbre Fiscal Digital (TFDv11):

```java
    CFDv33 cfd = new CFDv33(new FileInputStream(file));// Crea un CFD a partir de un InputStream
    TFDv11 tfd = new TFDv11(cfd); // Crea un TDF a partir del CDF
    PrivateKey key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile), password);
    tfd.timbrar(key); // Timbra el CDF
    tfd.verificar(cert); // Verifica el TDF
    tfd.guardar(System.out); // Serializa el CFD timbrado a un OutputStream
```

### Utiliza maven
```
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ...
        <dependency>
            <groupId>com.github.bigdata-mx</groupId>
            <artifactId>factura-electronica</artifactId>
            <version>0.3.0</version><!--La versión puede ser un TAG, RELEASE o GIT SHORT HASH-->
        </dependency>
    
    $ mvn install -U
    
```

## Documentos

* [Guia del usuario](https://github.com/bigdata-mx/factura-electronica/wiki/Guia-del-usuario)
* [Documentación del API](http://factura-electronica.googlecode.com/svn/javadoc/index.html)
* [Preguntas frecuentes](https://github.com/bigdata-mx/factura-electronica/wiki/Preguntas-frecuentes)
* [Linea de Comandos](https://github.com/bigdata-mx/factura-electronica/wiki/Linea-de-comandos)
* [Código fuente](https://github.com/bigdata-mx/factura-electronica/wiki/Compilar-el-codigo-fuente)

Valida tus CFD-I utilizando el [Validador de forma y sintaxis de Comprobantes Fiscales Digitales v3](https://www.consulta.sat.gob.mx/sicofi_web/moduloECFD_plus/ValidadorCFDI/Validador%20cfdi.html).

Encuentra más información sobre los CFDI en las siguientes referencias las siguientes referencias:

* [Comunicado de prensa](http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19339.html)
 [03/09/2010]
* [Nuevo esquema de facturación electrónica 2011](http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19209.html)
 [08/10/2010]
* [Información para proveedores de certificación de CFDI](http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19069.html)
 [22/10/2010]
* [Ejemplos y preguntas frecuentes de tecnología](http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19430.html)
 [22/10/2010]
* [Fundamento legal](http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_18889.html)
 [24/09/2010]
* [Validadores y referencias para los CFD v2](http://www.sat.gob.mx/sitio_internet/e_sat/comprobantes_fiscales/15_15565.html)
 [19/07/2010]

### Dudas y comentarios
¿Tienes algún problema o sugerencia de mejora?

Busca la respuesta en la sección de [preguntas frecuentes](https://github.com/bigdata-mx/factura-electronica/wiki/Preguntas-frecuentes) o en la sección de 
[seguimiento](https://github.com/bigdata-mx/factura-electronica/issues?state=open). Si no encuentras la respuesta, crea una nueva entrada utilizando 
la liga de New Issue y haremos todo lo posible por solucionarlo.
