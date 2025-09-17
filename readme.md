# Agrosmart App Móvil  🌽🫘🌱
AgroSmart Es una app móvil dedicada al diagnostico de deficiencias nutricionales en los cultivos del  
frijol, trigo y maíz, potenciada con machine learning e inteligencia artificial para elaborar los planes de fertilización en base a los diagnósticos.

Agrosmart también funciona como una plataforma para informarse y aprender el cuidado de los cultivos gracias la sección de novedades y noticias agrícolas, proporcionadas por las instituciones agrícolas mas importantes del país como el INTA y el IPSA.

## Funcionamiento básico de la app.
El agricultor afectado debe de tomar una fotografía del cultivo dentro de la app o seleccionar una foto de su galería , posterior a eso el modelo de machine learning analizara la fotografía para brindar un diagnostico basado en las múltiples deficiencias soportadas por la app como lo son, el nitrógeno, el potasio, el fosforo y el magnesio. Una vez analizada la foto se le da el resultado del diagnostico, luego si el agricultor dispone de internet en el momento se le proporcionara una recomendación para tratar el cultivo utilizando inteligencia artificial, la recomendación se guardara en el registro de la recomendación del cual fue generado.
En caso de que el agricultor no disponga de internet en el momento, podrá generar su recomendación posteriormente desde el registro del historial del ultimo diagnostico.

## Tecnologías de la aplicación
- Android Framework (Java 11)
- TensorFlow Lite
- Firesbase SDK
- MongoDB Realm
- Retrofit (Cliente HTTP)
- Gradle (Sistema de construcción)

## Requerimientos técnicos mínimos
- Android 7.0 o Superior
-  2GB de Ram
-  1Gb de almacenamiento
- Wifi o datos móviles

## Arquitectura de la aplicación
Agrosmart usa una Clean Arquiteture separando la lógica En Datos, Dominio y Presentación.
Complementando su arquitectura con el MVVM(Modelo-Vista-ViewModel), el cual es el patrón arquitectónico recomendado para el desarrollo de aplicaciones Android.
El cual se encarga de desacoplar las responsabilidades en capas independientes.
