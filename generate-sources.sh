cd sources || mkdir "sources" && cd sources || exit
for file in $(seq 1 2000); do
  touch file-"${file}".java
  for line in $(seq 1 100000); do
    echo "line $line" >> file-"${file}".java
  done
done
