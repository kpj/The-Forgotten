# Execute from root directory

output="The-Forgotten.jar"
src="src"
bin="bin"
tmp="temporary_compiler_directory"

mkdir $tmp

cp -r $bin/* $tmp
cp -r data $tmp

cd $tmp

echo "Main-Class: Startup" > manifest
jar cmf manifest $output .

mv $output ..
cd ..

rm -r $tmp
