# Makefile
.DEFAULT_GOAL := build-run

run-dist: #  Запуск исполняемого файла
	make -C ./hexlet-jdbc run-dist

setup:
	make -C ./hexlet-jdbc setup

clean:
	make -C ./hexlet-jdbc clean

build:
	make -C ./hexlet-jdbc build

install:
	make -C ./hexlet-jdbc install

run:
	make -C ./hexlet-jdbc run

test:
	make -C ./hexlet jdbc test

report:
	make -C ./hexlet-jdbc report

lint:
	make -C ./hexlet-jdbc lint

check-deps:
	make -C ./hexlet-jdbc check-deps


build-run: make -C ./hexlet-jdbc build run

.PHONY: build
