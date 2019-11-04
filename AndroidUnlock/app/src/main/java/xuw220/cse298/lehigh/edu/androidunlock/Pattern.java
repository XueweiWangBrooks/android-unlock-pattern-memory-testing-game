package xuw220.cse298.lehigh.edu.androidunlock;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pattern {
    private List<Vertex> vertices;
    private int order;

    private enum Direction {
        right, left, up, down
        //, rightUp, rightDown, leftUp, leftDown
    }

    public Pattern(int order) {
        this(new ArrayList<Vertex>());
        this.order = order;
    }

    public Pattern(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Pattern(String patternEncoded) {
        this.vertices = new ArrayList<>();
        String[] tokens = patternEncoded.split(" ");
        this.order = Integer.parseInt(tokens[0]);
        for (int i = 1; i < tokens.length - 1; i += 2) {
            Vertex vertex = new Vertex(Integer.parseInt(tokens[i]), Integer.parseInt(tokens[i + 1]));
            vertices.add(vertex);
        }
    }


    public boolean addVertex(Vertex v) {
        //check for repeated path.
        if (vertices.isEmpty()) {
            vertices.add(v);
            return true;
        }
        Vertex previousV = vertices.get(vertices.size() - 1);
        if (v.equals(previousV))
            return false;
        for (int i = 0; i < vertices.size() - 1; i++) {
            //if same path in the same dir
            if (vertices.get(i).equals(previousV) && vertices.get(i + 1).equals(v)) {
                return false;
            }
            //same path in the different dir
            if (vertices.get(i).equals(v) && vertices.get(i + 1).equals(previousV)) {
                return false;
            }
        }
        vertices.add(v);
        return true;
    }


    public Vertex registerMove(Vertex current, Direction direct) {
        Vertex previousVertex = current;
        switch (direct) {
            case right:
                current = new Vertex(current.getI(), current.getJ() + 1);
                break;
            case left:
                current = new Vertex(current.getI(), current.getJ() - 1);
                break;
            case up:
                current = new Vertex(current.getI() - 1, current.getJ());
                break;
            case down:
                current = new Vertex(current.getI() + 1, current.getJ());
                break;
//            case rightUp:
//                current = new Vertex(current.getI() - 1, current.getJ() + 1);
//                break;
//            case rightDown:
//                current = new Vertex(current.getI() + 1, current.getJ() + 1);
//                break;
//            case leftUp:
//                current = new Vertex(current.getI() - 1, current.getJ() - 1);
//                break;
//            case leftDown:
//                current = new Vertex(current.getI() + 1, current.getJ() - 1);
//                break;
        }
        if (addVertex(current)) {
            return current;
        }
        return previousVertex;
    }

    public int getVertexCount() {
        return vertices.size();
    }


    public Vertex getVertex(int index) {
        Log.i("vertex here", vertices.get(index).toString());
        return vertices.get(index);
    }

    public Vertex getVertex(Vertex v) {
        for (Vertex vertex : vertices) {
            if (v.equals(vertices)) {
                return vertex;
            }
        }
        return null;
    }


    public boolean doesVertexExists(Vertex newVertex) {
        for (Vertex vertex : vertices) {
            if (vertex.equals(newVertex))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder retval = new StringBuilder();
        for (Vertex vertex : vertices) {
            retval.append(vertex.toString());
            if (vertices.indexOf(vertex) != vertices.size() - 1)
                retval.append(", ");
        }
        return retval.toString();
    }

    public String encode() {
        StringBuilder retval = new StringBuilder();
        retval.append(order + " ");
        for (Vertex vertex : vertices) {
            retval.append(vertex.encode());
            if (vertices.indexOf(vertex) != vertices.size() - 1)
                retval.append(" ");
        }
        return retval.toString();
    }

    public static String encodeAsJson(List<Pattern> list) {
        StringBuilder resultJsonAsStr = new StringBuilder();
        resultJsonAsStr.append("[");
        for (Pattern pattern : list) {
            resultJsonAsStr.append("'" + pattern.encode() + "'");
            if (list.indexOf(pattern) < list.size() - 1) {
                resultJsonAsStr.append(",");
            }
        }
        resultJsonAsStr.append("]");
        return resultJsonAsStr.toString();
    }

    public int getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pattern)) {
            return false;
        }
        Pattern patternCompareTo = (Pattern) object;
        if (vertices.size() != patternCompareTo.vertices.size()) {
            return false;
        }
        for (int i = 0; i < vertices.size(); i++) {
            if (!vertices.get(i).equals(patternCompareTo.vertices.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static Pattern randomPatternGenerator(int order, int steps) {
        Pattern pattern = new Pattern(order);
        int ranRow = (int) (Math.random() * order - 1);
        int ranCol = (int) (Math.random() * order - 1);
        Vertex cur = new Vertex(ranRow, ranCol);
        pattern.addVertex(cur);
        for (int i = 0; i < steps; i++) {
            ArrayList<Direction> legalDirect = new ArrayList<Direction>(Arrays.asList(Direction.values()));
            if (cur.getI() == 0) {
                legalDirect.remove(Direction.up);
//                legalDirect.remove(Direction.leftUp);
//                legalDirect.remove(Direction.rightUp);
            }
            if (cur.getI() == order - 1) {
                legalDirect.remove(Direction.down);
//                legalDirect.remove(Direction.leftDown);
//                legalDirect.remove(Direction.rightDown);
            }
            if (cur.getJ() == 0) {
                legalDirect.remove(Direction.left);
//                legalDirect.remove(Direction.leftDown);
//                legalDirect.remove(Direction.leftUp);
            }
            if (cur.getJ() == order - 1) {
                legalDirect.remove(Direction.right);
//                legalDirect.remove(Direction.rightDown);
//                legalDirect.remove(Direction.rightUp);
            }
            Direction direct = legalDirect.remove((int) (Math.random() * legalDirect.size()));
            Vertex next = pattern.registerMove(cur, direct);
            while ((!legalDirect.isEmpty()) && next.equals(cur)){
                direct = legalDirect.remove((int) (Math.random() * legalDirect.size()));
                next = pattern.registerMove(cur, direct);
            }
            cur = next;
        }
        return pattern;
    }

}
