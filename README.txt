factura-electronica
====================

Librería de componentes Java para el desarrollo de aplicaciones de 
Factura Electrónica (CFDI)

=Librería de componentes=

La librería presenta una interfaz muy simple centrada en el Comprobante Fiscal
 Digital (CFD), las clases principales son CFDv3 y CFDv2 que tienen la lógica 
correspondiente a las versiones 3.0 y 2.0 del CFD respectivamente.

Para mayor información consulta:
* Guía del usuario, http://code.google.com/p/factura-electronica/wiki/GuiaDelUsuario
* Preguntas frecuentes, http://code.google.com/p/factura-electronica/wiki/PreguntasFrecuentes

==Instalación==

 1. Descarga la última versión de las librerías
 2. Descomprime el archivo cfdi-X.Y.Z-bin.zip o cfdi-X.Y.Z-bin.tar.gz

==Herramientas de línea de comandos==

La librería incluye dos programas que permiten trabajar con los CFD desde la 
linea de comandos: cfd para trabajar con la versión 2.0 y cfdi para trabajar 
con la versión 3.0. Para ejecutar estos comandos cambia al directorio donde se 
descomprimieron los archivos y ejecuta cualquiera de los siguientes ejemplos:

Nota: Se muestran los ejemplos para *nix, seguidos de los ejemplos para windows.

* Para validar un CFD sellado contra el esquema:
# v3.0:
./bin/cfdi validar ejemplos/cfdv3.externo.xml
# v2.0:
./bin/cfd validar ejemplos/cfdv2.externo.xml

rem v3.0:
.\bin\cfdi validar ejemplos\cfdv3.externo.xml 
rem v2.0:
.\bin\cfd validar ejemplos\cfdv2.externo.xml 


* Para verificar el sello del CFDI:
# v3.0
./bin/cfdi verificar ejemplos/cfdv3.externo.xml 
# v2.0
./bin/cfd verificar ejemplos/cfdv2.externo.xml 

rem v3.0:
.\bin\cfdi verificar ejemplos\cfdv3.externo.xml 
rem v2.0:
.\bin\cfd verificar ejemplos\cfdv2.externo.xml 


* Para sellar el CFDI:
# v3.0:
./bin/cfdi sellar ejemplos/cfdv3.xml ejemplos/emisor.key a0123456789 ejemplos/emisor.cer cfdv3_sellado.xml 
# v2.0:
./bin/cfd sellar ejemplos/cfdv2.xml ejemplos/emisor.key a0123456789 ejemplos/emisor.cer cfdv2_sellado.xml 

rem v3.0:
.\bin\cfdi sellar ejemplos\cfdv3.xml ejemplos\emisor.key a0123456789 ejemplos\emisor.cer cfdv3_sellado.xml 
rem v2.0:
.\bin\cfd sellar ejemplos\cfdv2.xml ejemplos\emisor.key a0123456789 ejemplos\emisor.cer cfdv2_sellado.xml 


* Para verificar un CFDI timbrado:
# v3.0:
./bin/cfdi verificar-timbrado ejemplos/cfdv3.externo.xml ejemplos/pac.cer

rem v3.0:
.\bin\cfdi verificar-timbrado ejemplos\cfdv3.externo.xml ejemplos\pac.cer


* Funcionalidad adicional, para timbrar el CFDI:
# v3.0:
./bin/cfdi timbrar ejemplos/cfdv3.externo.xml ejemplos/pac.key a0123456789 ejemplos/pac.cer cfdv3_timbrado.xml

rem v3.0:
.\bin\cfdi timbrar ejemplos\cfdv3.externo.xml ejemplos\pac.key a0123456789 ejemplos\pac.cer cfdv3_timbrado.xml

Nota: Estos comandos no regresan ningún mensaje si la operación fue exitosa.

==Código fuente==

Los usuarios avanzados pueden estar interesados en descargar y compilar el 
código fuente, para ello es necesario seguir los siguientes pasos:

===Pre-requisitos===
Para trabajar con el código fuente de la librería de factura electrónica debes
 tener instalados los siguientes sistemas:

* Java 6
* Maven

===Obtener el código fuente===
Puedes obtener el código fuente de las librerías en la siguiente dirección: 
http://code.google.com/p/factura-electronica/source/checkout

Revisa la información en las preguntas frecuentes para agregar el código fuente
a un IDE (Netbeans o Eclipse)

===Compilar los componentes===
Para compilar los componentes, utiliza el comando:

 mvn compile

este comando preparará todas las dependencias y generará el código necesario 
para trabajar con el XSD de la versión 3.0 y 2.0 del CFD y la versión 1.0 del 
TFD.

===Ejecutar el programa de ejemplo===
Para ejecutar el programa de ejemplo, utiliza el comando:

 mvn exec:java

==Dudas y comentarios==
¿Tienes algún problema o sugerencia de mejora?

Busca la respuesta en la sección de preguntas frecuentes o en la sección de 
seguimiento. Si no encuentras la respuesta, crea una nueva entrada utilizando 
la liga de New Issue y haremos todo lo posible por solucionarlo.
