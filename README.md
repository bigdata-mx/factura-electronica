factura-electronica
====================

Librería de componentes Java para el desarrollo de aplicaciones de 
Factura Electrónica (CFDI)

## Librería de componentes

La librería presenta una interfaz muy simple centrada en el Comprobante Fiscal
Digital (CFD), las clases principales son CFDv3 y CFDv2 que tienen la lógica 
correspondiente a las versiones 3.0 y 2.0 del CFD respectivamente.

Cada uno de estos elementos tiene la funcionalidad necesaria para: validar, firmar, verificar y serializar CFDs.

### Comprobante Fiscal Digital por Internet (CFDv32):

```java
    CFDv32 cfd = new CFDv32(new FileInputStream(file)); // Crea un CFD a partir de un InputStream
    Key key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile),  password);
    Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile));
    Comprobante sellado = cfd.sellarComprobante(key, cert); // Firma el CFD y obtiene un Comprobante sellado
    cfd.validar(); // Valida el XML, que todos los elementos estén presentes
    cfd.verificar(); // Verifica un CFD ya firmado
    cfd.guardar(System.out); // Serializa el CFD a un OutputStream
```

### Comprobante Fiscal Digital  (CFDv22):

```java
    CFDv22 cfd = new CFDv22(new FileInputStream(file)); // Crea un CFD a partir de un InputStream
    Key key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile),  password);
    Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile));
    Comprobante sellado = cfd.sellarComprobante(key, cert); // Firma el CFD y obtiene un Comprobante sellado
    cfd.validar(); // Valida el XML, que todos los elementos estén presentes
    cfd.verificar(); // Verifica un CFD ya firmado
    cfd.guardar(System.out); // Serializa el CFD a un OutputStream
```

### Timbre Fiscal Digital (TFDv1):

```java
    CFDv32 cfd = new CFDv32(new FileInputStream(file));// Crea un CFD a partir de un InputStream
    TFDv1 tfd = new TFDv1(cfd); // Crea un TDF a partir del CDF
    PrivateKey key = KeyLoader
        .loadPKCS8PrivateKey(new FileInputStream(keyfile), password);
    tfd.timbrar(key); // Timbra el CDF
    tfd.verificar(cert); // Verifica el TDF
    tfd.guardar(System.out); // Serializa el CFD timbrado a un OutputStream
```

## Instalación

### Instalación

 1. Descarga la última versión de las librerías
 2. Descomprime el archivo cfdi-X.Y.Z-bin.zip o cfdi-X.Y.Z-bin.tar.gz

### Documentos

* [Linea de Comandos](https://github.com/bigdata-mx/factura-electronica/wiki/Linea-de-comandos)
* [Código fuente](https://github.com/bigdata-mx/factura-electronica/wiki/Compilar-el-codigo-fuente)
* [Guia del usuario](https://github.com/bigdata-mx/factura-electronica/wiki/Guia-del-usuario)
* [Preguntas frecuentes]
* [Documentación del API][http://factura-electronica.googlecode.com/svn/javadoc/index.html]

Entérate de las mejoras y actualizaciones a las librerías a través de nuestra [cuenta de twitter]([http://www.twitter.com/bigdata_mx)

Valida tus CFD-I utilizando el [https://www.consulta.sat.gob.mx/sicofi_web/moduloECFD_plus/ValidadorCFDI/Validador%20cfdi.html Validador de forma y sintaxis de Comprobantes Fiscales Digitales v3]


Encuentra más información sobre los CFDI en las siguientes referencias las siguientes referencias:

  * [http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19339.html Comunicado de prensa] `[`03/09/2010`]`
  * [http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19209.html Nuevo esquema de facturación electrónica 2011] `[`08/10/2010`]`
  * [http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19069.html Información para proveedores de certificación de CFDI] `[`22/10/2010`]`
  * [http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_19430.html Ejemplos y preguntas frecuentes de tecnología] `[`22/10/2010`]`
  * [http://www.sat.gob.mx/sitio_internet/asistencia_contribuyente/principiantes/comprobantes_fiscales/66_18889.html Fundamento legal] `[`24/09/2010`]`
  * [http://www.sat.gob.mx/sitio_internet/e_sat/comprobantes_fiscales/15_15565.html Validadores y referencias para los CFD v2] `[`19/07/2010`]`


### Dudas y comentarios
¿Tienes algún problema o sugerencia de mejora?

Busca la respuesta en la sección de preguntas frecuentes o en la sección de 
seguimiento. Si no encuentras la respuesta, crea una nueva entrada utilizando 
la liga de New Issue y haremos todo lo posible por solucionarlo.
