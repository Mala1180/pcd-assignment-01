cd source || mkdir "source" && cd source || exit
for file in $(seq 1 2000); do
  touch file-"${file}".java
  for line in $(seq 1 5000); do
    echo "line $line" >> file-"${file}".java
  done
done
