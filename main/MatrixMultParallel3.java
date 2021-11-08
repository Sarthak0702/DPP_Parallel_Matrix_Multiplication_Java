package main;

public class MatrixMultParallel3 implements Runnable
{
    private int i;
    private int jStart, jEnd;
    private Matrix a, r, c;

    public MatrixMultParallel3(int i, int jStart, int jEnd, Matrix a, Matrix r, Matrix c)
    {
        this.i = i;
        this.jStart = jStart;
        this.jEnd = jEnd;
        this.a = a;
        this.r = r;
        this.c = c;
    }

    @Override
    public void run()
    {
        for (int j = jStart; j < jEnd; j++)
            c.getData()[i][j] = Matrix.scalarProduct(a, i, r, j);
    }
}
