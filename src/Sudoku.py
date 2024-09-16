import sys
import math

class Sudoku:
    boardSize = 0
    partitionSize = 0
    fileNum = ""
    emptyLocations = [] #.append will put the item on the top of the stack, and .pop will take it off

    def main(self):
        filename = sys.argv[1]
        input = open(filename, "r")

        filenum = ""
        for letter in filename:
            if letter.isdigit():
                filenum += letter

        self.boardsize = int(input.readline())
        self.partitionsize = int(math.sqrt(self.boardsize))
        board = [range(self.boardsize) for x in range(self.boardsize)]

        print("Boardsize: " + str(self.boardsize) + " Partitionsize: " + str(self.partitionsize))

        print("Input:")
        i = 0
        for line in input:
            board[i] = [int(num) for num in (line.strip('\n').split(" "))]
            for j in range(len(board[i])):
                if board[i][j] == 0:
                    emptycell = [i, j]
                    self.emptyLocations.append(emptycell)
                print(board[i][j], end=' ')
            print()
            i += 1
        input.close()

        solved = self.solve(board)

        output = open(filename.strip(".txt") + "Solution.txt", "w")
        if not solved:
            print("No solution found.")
            output.write(str(-1))
        else:
            print("Output:\n" + str(filenum))
            output.write(str(filenum) + "\n")
            for row in board:
                for cell in row:
                    print(cell, end=' ')
                    output.write(str(cell))
                print()
                output.write("\n")
        output.close()


    def solve(self, board):
        if self.emptyLocations == []:
            return True

        emptyCell = self.emptyLocations.pop()
        for n in range(self.boardSize):
            if self.isViable(board, emptyCell, n):
                board[emptyCell[0]][emptyCell[1]] = n
                if self.solve(board):
                    return True
                board[emptyCell[0]][emptyCell[1]] = 0

        self.emptyLocations.append(emptyCell)
        return False


    def isViable(self, board, cell, n):
        row = cell[0]
        col = cell[1]

        for i in range(self.boardSize):
            if board[row][i] == n:
                return False
            if board[i][col] == n:
                return False

        startrow = row - (row % self.partitionsize)
        startcol = col - (col % self.partitionsize)
        for j in range(self.partitionsize):
            for k in range(self.partitionsize):
                if board[startrow + j][startcol + k] == n and (not(startrow == row and startcol == col)):
                    return False

        return True


if __name__ == "__main__":
    Sudoku().main()