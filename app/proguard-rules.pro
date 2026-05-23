# kotlinx.serialization keeps generated serializers via the compiler plugin.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class **$$serializer { *; }
