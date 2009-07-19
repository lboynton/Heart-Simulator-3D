/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartsimulator3d.ca;

import heartsimulator3d.GeometryToVtk;
import vtk.vtkImageData;

/**
 *
 * @author Lee Boynton
 */
public class NishiyamaTest
{
    public static void main(String args[])
    {
        Nishiyama nish = new Nishiyama();
        try
        {
            nish.readCellsFile();
        }
        catch (Exception ex)
        {
            System.err.println("Heart geometry file not found");
            System.exit(1);
        }
        nish.initCells();

        for(int i =0; i<100; i++)
        {
            nish.step();
        }

        GeometryToVtk g = new GeometryToVtk();
        vtkImageData image = g.getImageData(nish.getU());
        g.writeVtkFile(image, "HeartImageWave.vtk");
    }
}