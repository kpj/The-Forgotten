output="The-Forgotten.jar"

echo "Main-Class: Startup" > manifest
jar cmf manifest $output *.class pics data
rm manifest
