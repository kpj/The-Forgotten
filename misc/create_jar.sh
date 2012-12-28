output="The-Forgotten.jar"

python3 misc/compiler.py

echo "Main-Class: Startup" > manifest
jar cmf manifest $output *.class pics sounds data
rm manifest
