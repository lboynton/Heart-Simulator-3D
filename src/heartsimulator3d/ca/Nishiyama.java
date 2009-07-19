/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartsimulator3d.ca;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * 3D implementation of the cellular automata model by A. Nishiyamaa, H. Tanakab
 * and T. Tokihiroa presented in "An isotropic cellular automaton for excitable
 * media"
 * @author Lee Boynton
 */
public class Nishiyama
{
    private int xSize = 100; // size of the x axis
    private int ySize = 100; // size of the y axis
    private int zSize = 100; // size of the z axis
    private int N = 5; //
    private int delta1 = 3; // first delta value
    private int delta2 = 7; // second delta value
    private int u[][][] = new int[xSize][ySize][zSize]; // voltage values for each cell
    private int v[][][] = new int[xSize][ySize][zSize]; // recovery values for each cell
    private int delta[][][] = new int[xSize][ySize][zSize]; // delta values for each cell
    private int tempu[][][] = new int[xSize][ySize][zSize]; // temporary storage of cell values
    private boolean cells[][][] = new boolean[xSize][ySize][zSize]; // true/false if there is a cell

    public int[][][] getU()
    {
        return u;
    }

    public void setN(int N)
    {
        this.N = N;
    }

    public void setDelta1(int delta1)
    {
        this.delta1 = delta1;
    }

    public void setDelta2(int delta2)
    {
        this.delta2 = delta2;
    }

    private void readCellsFile() throws FileNotFoundException, IOException
    {
        FileInputStream fstream = new FileInputStream("./heart_lattice_3d");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;

        while ((strLine = br.readLine()) != null)
        {
            String coords[] = strLine.split(" ");

            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int z = Integer.parseInt(coords[2]);

            cells[x][y][z] = true;
        }

        in.close();
    }

    public void initCells()
    {
        Random generator = new Random();

        u = new int[xSize][ySize][zSize]; // voltage values for each cell
        v = new int[xSize][ySize][zSize]; // recovery values for each cell
        delta = new int[xSize][ySize][zSize]; // delta values for each cell
        tempu = new int[xSize][ySize][zSize]; // temporary storage of cell values
        cells = new boolean[xSize][ySize][zSize]; // true/false if there is a cell

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                for (int z = 0; z < zSize; z++)
                {
                    // initialise voltage values
                    u[x][y][z] = 0;

                    // initialise recovery values
                    v[x][y][z] = 0;

                    // initialise cells
                    cells[x][y][z] = false;

                    // initialise delta values
                    int randomDelta = generator.nextInt(2);

                    if (randomDelta == 0)
                    {
                        delta[x][y][z] = delta1;
                    }
                    else
                    {
                        delta[x][y][z] = delta2;
                    }
                }
            }
        }

        u[11][42][40] = 1;

        try
        {
            readCellsFile();
        }
        catch (Exception ex)
        {
            System.err.println("Could not open cells file");
            System.err.println(ex.toString());
            System.exit(1);
        }
    }

    private void copyIntoTemp()
    {
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                for (int z = 0; z < zSize; z++)
                {
                    tempu[x][y][z] = u[x][y][z];
                }
            }
        }
    }

    public void step()
    {
        // create copy of voltage values
        copyIntoTemp();

        for (int x = 1; x < xSize - 1; x++)
        {
            for (int y = 1; y < ySize - 1; y++)
            {
                for (int z = 0; z < zSize; z++)
                {
                    if (!cells[x][y][z])
                    {
                        continue;
                    }

                    if (u[x][y][z] == 0)
                    {
                        if (v[x][y][z] == 0)
                        {
                            // check for stimulation
                            // inefficient count of neighbours
                            if (tempu[x - 1][y - 1][z - 1] + tempu[x - 1][y - 1][z + 1] +
                                    tempu[x - 1][y - 1][z] + tempu[x - 1][y + 1][z - 1] +
                                    tempu[x - 1][y + 1][z + 1] + tempu[x - 1][y + 1][z] +
                                    tempu[x - 1][y][z - 1] + tempu[x - 1][y][z + 1] +
                                    tempu[x - 1][y][z] + tempu[x + 1][y - 1][z - 1] +
                                    tempu[x + 1][y - 1][x + 1] + tempu[x + 1][y - 1][z] +
                                    tempu[x + 1][y + 1][z - 1] + tempu[x + 1][y + 1][z + 1] +
                                    tempu[x + 1][y + 1][z] + tempu[x + 1][y][z - 1] +
                                    tempu[x + 1][y][z + 1] + tempu[x + 1][y][z] +
                                    tempu[x][y - 1][z - 1] + tempu[x][y - 1][z + 1] +
                                    tempu[x][y - 1][z] + tempu[x][y + 1][z - 1] +
                                    tempu[x][y + 1][z + 1] + tempu[x][y + 1][z] +
                                    tempu[x][y][z - 1] + tempu[x][y][z + 1] >= delta[x][y][z])
                            {
                                u[x][y][z] = 1;  // stimulated
                            }
                        }
                        else
                        {
                            v[x][y][z]--;  // refractory
                        }
                    }
                    else if (v[x][y][z] == N - 1)
                    {
                        u[x][y][z]--;  // downstoke
                    }
                    else if (u[x][y][z] == N - 1)
                    {
                        v[x][y][z]++;  // plateau
                    }
                    else
                    {
                        u[x][y][z]++;  // upstroke
                    }
                }
            }
        }
    }
}
