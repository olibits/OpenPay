# OpenPay

**OpenPay** is a sample Android application 📱 built to demonstrate use of *Modern Android development*.

## About

[Spanish instructions]
Crear una aplicación móvil para SO Android, con lenguaje de programación Kotlin, librerías de  compatibilidad, API mínima 21, guardar el código en un repositorio, la aplicación debe contar con una actividad principal y fragmentos para mostrar las funcionalidades que se solicitan.

- Consumir un servicio [API Rest](https://www.themoviedb.org/documentation/api) se puede acceder creando una cuenta gratuita
- Persistir los datos que devuelva el servicio de manera local y mostrar los datos al usuario  con una UI amigable e intuitiva, dichos datos deben estar disponibles cuando el dispositivo móvil no tenga conexión a Internet (modo offline).
- Al ingresar a la aplicación se deberá mostrar un Bottom Navigation con 4 opciones.
    - Pantalla 1: Pantalla de perfil, incluir información del usuario más popular con una UI amigable e intuitiva, donde se pueda visualizar reseñas hechas por el usuario, e imágenes.
    - Pantalla 2: Cargar la lista de todas las películas con una UI amigable e intuitiva separadas con  las películas más populares, las más calificadas y las mejores recomendaciones.
    - Pantalla 3: Consumir desde la consola de Firebase (Cloud Firestore) y mostrar las ubicaciones en un Mapa mostrando adicionalmente la fecha de almacenamiento usando una UI amigable e intuitiva.
      Agregar la ubicación del dispositivo a una consola de Firebase para persistir (Cloud Firestore) cada 5 minutos y notificar al usuario mediante NotificationCompat
    - Pantalla 4: Capturar o seleccionar de la galería del dispositivo una o varias imágenes y subirlas a Firebase Storage.

## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
    - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
    - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
    - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) -
    - [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Moshi](https://github.com/square/moshi) - A modern JSON library for Kotlin and Java.
- [Moshi Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/moshi) - A Converter which uses Moshi for serialization to and from JSON.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.



**Contributed By:** [Olimpia Saucedo](https://github.com/olibits/)


# Package Structure

     com.openpay.android    # Root Package
    .
    ├── data                # For data handling.
    │   ├── local           # Local Persistence Database. Room (SQLite) database
    |   │   ├── dao         # Data Access Object for Room   
    │   ├── remote          # Remote Data Handlers     
    |   │   ├── api         # Retrofit API for remote end point.
    │   └── repository      # Single source of data.
    |
    ├── model               # Model classes
    |
    ├── di                  # Dependency Injection Modules   
    |
    ├── ui                  # Activity/View layer - ViewModel Adapter ViewHolder
    |
    └── utils               # Utility Classes / Kotlin extensions


## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

## Screenshots

- Screen 1 Profile
 
<img src="https://github.com/olibits/OpenPay/blob/master/screen_one_profile.jpg"  width="300" height="600">  <img src="https://github.com/olibits/OpenPay/blob/master/screen_one_profile_collapsed.jpg"  width="300" height="600">

- Screen 2 Movies

<img src="https://github.com/olibits/OpenPay/blob/master/screen_two_movies.jpg"  width="300" height="600"> 

- Screen 3 Upload image to Firebase Storage

    - Firebase Storage console
    
<img src="https://github.com/olibits/OpenPay/blob/master/firebase_storage_console.png"  width="600" height="300"> 

    - Upload image from gallery
    - Ask image source -> Select from gallery -> Preview image selected from gallery
    
<img src="https://github.com/olibits/OpenPay/blob/master/screen_three_dialog.jpg"  width="150" height="300">  <img src="https://github.com/olibits/OpenPay/blob/master/screen_three_pickeddromgallery.jpg"  width="150" height="300"> 

    - Upload image from camera
    - Selected source from camera -> Ask for permissions- -> Preview of camera -> Photo taken
<img src="https://github.com/olibits/OpenPay/blob/master/screen_three_permissions.jpg"  width="150" height="300"> <img src="https://github.com/olibits/OpenPay/blob/master/screen_three_camera_preview.jpg"  width="150" height="300"> <img src="https://github.com/olibits/OpenPay/blob/master/screen_three_phototaken.jpg"  width="150" height="300"> 

    - Image Uploaded
 <img src="https://github.com/olibits/OpenPay/blob/master/screem_three_image_upload.jpg"  width="150" height="300"> 
 
- Screen 4
    - Show the location of the device that is stored in firebase every 5 minutes
        
    - Firebase database console
    
 <img src="https://github.com/olibits/OpenPay/blob/master/firebase_database_console.png" width="600" height="300">
 
    - Map showing the locations and markers
    
   <img src="https://github.com/olibits/OpenPay/blob/master/screen_map.png" width="300" height="600">
    
    - Notification 
    
   <img src="https://github.com/olibits/OpenPay/blob/master/notification.png" width="300" height="600">
   
   - If the internet connection is not available the data is retrieved from local
   
   <img src="https://github.com/olibits/OpenPay/blob/master/no_internet_profile.jpg" width="300" height="600">  <img src="https://github.com/olibits/OpenPay/blob/master/no_internet_movies.jpg" width="300" height="600">     


    

## Technical debt 💬

- Unit test
- Remove Hardcode 
- Take care of missing best practices
- Edge cases


## Contact
If you need any help, you can connect with me.

Email:- [olimpiasaucedo@gmail.com]
