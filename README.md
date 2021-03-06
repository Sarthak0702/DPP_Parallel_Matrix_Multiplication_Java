# Parallel Matrix Multiplication

- Matrix Multiplication was implimented using `java.util.concurrent`'s `ExecutorService`, `Executors`, `Future` class and interface.
- `MatrixMultParallel3` implements Matrix Multiplication columns-wise each process calculating result of block  of size (1 x N/p) 
- `MatrixMultParallel3` implements Matrix Multiplication row-wise each process calculating result of block  of size (N/p x N)



# Results

Available processes: 8

Correctness  true

|  N    |  Seq(ms)    |  PrlCol(ms)   | PrlRow(ms)  | spdUpRow | spdUpRow  |
| ----- | -------- | --------- | ------- | -------- | --------- |
| 64    |  4.96    |   20.88   |  3.48   |  0.24    |   1.43    |
| 128   |  7.44    |   11.15   |  2.30   |  0.67    |   3.23    |
| 256   |  3.03    |   15.99   |  5.51   |  0.19    |   0.55    |
| 512   |  13.13   |   22.21   |  30.88  |  0.59    |   0.43    |
| 1024  |  47.97   |   48.83   |  11.23  |  0.98    |   4.27    |
| 2048  |  210.27  |   89.51   |  19.77  |  2.35    |   10.64   |
| 4096  |  926.51  |   225.05  |  81.19  |  4.12    |   11.41   |   