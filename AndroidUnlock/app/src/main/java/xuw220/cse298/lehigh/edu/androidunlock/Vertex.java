package xuw220.cse298.lehigh.edu.androidunlock;

public class Vertex {
    private int i;
    private int j;

    public Vertex(int row, int col) {
        this.i = row;
        this.j = col;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return "{" + i + ", " + j + "}";
    }

    public String encode(){
        return i + " " + j;
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex)) {
            return false;
        }
        Vertex vertexCompareTo = (Vertex) obj;
        return vertexCompareTo.getI() == i && vertexCompareTo.getJ() == j;
    }
}
