/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartsimulator3d;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import vtk.vtkDataArray;
import vtk.vtkImageData;
import vtk.vtkPanel;
import vtk.vtkStructuredPointsWriter;

public class GeometryToVtk
{
    public static void main(String args[])
    {
        vtkPanel panel = new vtkPanel();
        vtkImageData image = new vtkImageData();
        image.SetDimensions(100, 100, 100);
        image.AllocateScalars();
        vtkDataArray array = image.GetPointData().GetScalars();

        try
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

                int tuple = x + (y * 100) + (z * 10000);

                array.SetTuple1(tuple, 1);
            }

            in.close();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }

        vtkStructuredPointsWriter writer = new vtkStructuredPointsWriter();
        writer.SetFileName("HeartImage.vtk");
        writer.SetInput(image);
        writer.Write();
    }

    public vtkImageData getImageData(int[][][] geometry)
    {
        vtkPanel panel = new vtkPanel();
        vtkImageData image = new vtkImageData();
        image.SetDimensions(100, 100, 100);
        image.AllocateScalars();
        vtkDataArray array = image.GetPointData().GetScalars();

        for (int x = 0; x < geometry.length; x++)
        {
            for (int y = 0; y < geometry[x].length; y++)
            {
                for (int z = 0; z < geometry[x][y].length; z++)
                {
                    double opacity = ((double) geometry[x][y][z]) / 4;

                    int tuple = x + (y * 100) + (z * 10000);

                    array.SetTuple1(tuple, opacity);
                }
            }
        }

        return image;
    }

    public void writeVtkFile(vtkImageData image, String filename)
    {
        vtkStructuredPointsWriter writer = new vtkStructuredPointsWriter();
        writer.SetFileName(filename);
        writer.SetInput(image);
        writer.Write();
    }
}
