build: bin/main

bin/main: lib/libscalar.a lib/libutil.so src/main.cpp include/logger.h
	mkdir -p bin
	g++ src/main.cpp -L./lib -lscalar -lutil -o $@

build/main.o: src/main.cpp
	mkdir -p build
	g++ -c $^ -o $@

build/logger.o: src/logger.cpp
	mkdir -p build
	g++ -c $^ -o $@

build/matrix.o: src/matrix.cpp
	mkdir -p bui
	g++ -c $^ -o $@

build/scalar.o: src/scalar.cpp
	mkdir -p build
	g++ -c $^ -o $@

libs: lib/libscalar.a lib/libutil.so

lib/libscalar.a: build/main.o build/logger.o build/scalar.o include/logger.h include/scalar.h
	mkdir -p lib
	ar rcs $@ -o $^

lib/libutil.so: src/main.cpp src/logger.cpp src/matrix.cpp include/logger.h include/matrix.h
	mkdir -p lib
	g++ -fPIC $^ -shared -o $@

run: run.sh
	bash run.sh 0
	mv log_file ./build/

tests: run.sh
	bash run.sh 1
	bash run.sh 2
	bash run.sh 3
	bash run.sh 4
	bash run.sh 5
	mv log_file ./build/

test1: run.sh
	bash run.sh 1
	mv log_file ./build/

test2: run.sh
	bash run.sh 2
	mv log_file ./build/

test3: run.sh
	bash run.sh 3
	mv log_file ./build/

test4: run.sh
	bash run.sh 4
	mv log_file ./build/

test5: run.sh
	bash run.sh 5
	mv log_file ./build/

clean:
	rm -rf build lib bin

.PHONY: clean build tests
