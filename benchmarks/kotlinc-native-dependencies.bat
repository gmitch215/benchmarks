@rem Trigger compiler - Installes Native Dependencies
%KOTLIN_NATIVE_HOME%\bin\kotlinc-native.bat benchmarks/array-1K/main.kt -o main.kt.old -opt

@rem Cleanup
del main.kt.old.exe