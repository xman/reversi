CXX = g++
CXXFLAGS = -DNDEBUG -O3 -Wall -g 

all:
	$(CXX) $(CXXFLAGS) main.cpp board.cpp evaluate.cpp gen_move.cpp -o reversi

test: 
	time -p ./reversi



clean:
	rm -f reversi

