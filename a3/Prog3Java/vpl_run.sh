#! /bin/bash
cat > vpl_execution << 'EOF'
#! /bin/bash
javac -cp .:jgrapht-core-1.4.0.jar:jheaps-0.13.jar _Main.java
java -cp .:jgrapht-core-1.4.0.jar:jheaps-0.13.jar _Main
EOF
chmod +x vpl_execution