# Trigger compiler - Installes Native Dependencies
kotlinc-native benchmarks/array-1K/main.kt -o main.kt.old -opt

# Cleanup
rm -rf benchmarks/array-1K/main.kt.kexe