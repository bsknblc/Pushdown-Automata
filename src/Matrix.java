import java.util.Arrays;
import java.util.List;

public class Matrix {
    String[][] Matrix;
    int numberOfRows;
    int numberOfColumns;

    public Matrix (int numberOfStates, int numberOfColumns){
        this.numberOfRows=numberOfStates;
        this.numberOfColumns=numberOfColumns;
        this.Matrix = new String[numberOfStates][numberOfColumns];
    }

    public String[][] getMatrix() {
        return Matrix;
    }

    public void setMatrix(String[][] matrix) {
        Matrix = matrix;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public void setMatrixValues(int first, int second, State value, String push){
        this.Matrix[first][second]=value+" "+push;
    }

    public String getMatrixValues(int first, int second){
        return this.Matrix[first][second];
    }

    @Override
    public String toString() {
        String mat="";
        for(int i=0; i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns;j++){
                mat= mat+ "["+this.Matrix[i][j]+"]"+" ";
            }
            mat = mat+ "\n";
        }
        return mat;
    }
}
