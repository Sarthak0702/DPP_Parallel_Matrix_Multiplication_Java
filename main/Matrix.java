package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Matrix
{
    private static Random rnd = new Random();

    private int rows, cols;
    private float[][] data;

    private static void validate(Matrix a, Matrix b)
    {
        if (a.rows != b.cols || a.cols != b.rows)
            throw new IllegalArgumentException("rows of A must be equal to columns of B & columns of A must be equal to rows of B!");
    }

    private static int getMaxThreadCount(int len)
    {
        return Math.min(len, Runtime.getRuntime().availableProcessors());
    }

    private static void wait(ArrayList<Future> futures)
    {
        for (Future f : futures)
        {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        futures.clear();
    }

    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        this.data = new float[rows][cols];
    }

    public Matrix(float[][] data)
    {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public static Matrix createRandomized(int rows, int cols)
    {
        Matrix m = new Matrix(rows, cols);

        for (int i = 0; i < m.rows; i++)
            for (int j = 0; j < m.cols; j++)
                m.data[i][j] = rnd.nextFloat() * 100;

        return m;
    }

    public static Matrix transposed(Matrix a)
    {
        Matrix r = new Matrix(a.cols, a.rows);

        for (int i = 0; i < r.rows; i++)
            for (int j = 0; j < r.cols; j++)
                r.data[i][j] = a.data[j][i];

        return r;
    }

    public static float scalarProduct(Matrix a, int aRow, Matrix b, int bRow)
    {
        float scalar = 0.0f;
        for (int k = 0; k < b.getCols(); k++)
            scalar += a.data[aRow][k] * b.data[bRow][k];

        return scalar;
    }

    public static Matrix multSerial(Matrix a, Matrix b)
    {
        validate(a, b);

        Matrix c = new Matrix(a.rows, b.cols);

        for (int i = 0; i < c.rows; i++)
            for (int j = 0; j < c.cols; j++)
                for (int k = 0; k < b.rows; k++)
                    c.data[i][j] += a.data[i][k] * b.data[k][j];

        return c;
    }

    public static Matrix multParallel3(Matrix a, Matrix b)
    {
        validate(a, b);

        Matrix c = new Matrix(a.rows, b.cols);
        Matrix r = Matrix.transposed(b);

        //TC=8
        final int threadCount = getMaxThreadCount(c.rows);
        //128
        final int minRowsColsPerThread = c.rows / threadCount;
        int extraRowsCols = c.rows - minRowsColsPerThread * threadCount;

        ArrayList<Future> futures = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < c.rows; i++)
        {
            for (int x = 0, j = 0; x < threadCount; x++, j += minRowsColsPerThread)
            {
                int cols = (x < threadCount-1 ? minRowsColsPerThread : minRowsColsPerThread + extraRowsCols);
                futures.add(executor.submit(new MatrixMultParallel3(i, j, j + cols, a, r, c)));
            }
            
            wait(futures);
        }

        executor.shutdown();
        return c;
    }

    public static Matrix multParallel4(Matrix a, Matrix b)
    {
        validate(a, b);

        Matrix c = new Matrix(a.rows, b.cols);
        Matrix r = Matrix.transposed(b);

        final int threadCount = getMaxThreadCount(c.rows);
        final int minRowsColsPerThread = c.rows / threadCount;
        int extraRowsCols = c.rows - minRowsColsPerThread * threadCount;

        ArrayList<Future> futures = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int x = 0, i = 0; x < threadCount; x++, i += minRowsColsPerThread)
        {
            int rows = (x < threadCount-1 ? minRowsColsPerThread : minRowsColsPerThread + extraRowsCols);
            futures.add(executor.submit(new MatrixMultParallel4(i, i + rows, a, r, c)));
        }

        wait(futures);

        executor.shutdown();
        return c;
    }

    public void print()
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                System.out.printf("%5.0f ", data[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public float[][] getData()
    {
        return data;
    }

    @Override
    public boolean equals(Object obj)
    {
        Matrix b = (Matrix)obj;

        if (rows != b.rows || cols != b.cols)
            return false;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (Math.abs(data[i][j] - b.data[i][j]) > 10E-6)
                    return false;

        return true;
    }
}
