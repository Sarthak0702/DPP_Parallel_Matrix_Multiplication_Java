package main;

public class MatrixMultParallel4 implements Runnable
{
    private int iStart, iEnd;
    private Matrix a, r, c;

    public MatrixMultParallel4(int iStart, int iEnd, Matrix a, Matrix r, Matrix c)
    {
        this.iStart = iStart;
        this.iEnd = iEnd;
        this.a = a;
        this.r = r;
        this.c = c;
    }

    @Override
    public void run()
    {
        for (int i = iStart; i < iEnd; i++)
            for (int j = 0; j < c.getCols(); j++)
                c.getData()[i][j] = Matrix.scalarProduct(a, i, r, j);
    }
}
