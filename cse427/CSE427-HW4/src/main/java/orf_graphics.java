import ij.gui.Plot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class orf_graphics {

    private static ArrayList<double[]> REPORT; //report from previous steps
    private Plot p; //must have dependency, see pom.xml for more info

    public orf_graphics(ArrayList<double[]> fullReport){
        this.REPORT = fullReport;
    }

    public void generateROC() throws IOException {
        double P = 0;
        double N = 0;
        double maxLength = 0;
        double maxMM = 0;
        for(double[] d : REPORT){
            if(d[2] > maxLength)
                maxLength = d[2];
            if(d[3] > maxMM)
                maxMM = d[3];
            if(d[4] == 1)
                P++;
            else
                N++;
        }

        double[] TPR = new double[(int) maxLength];
        double[] FPR = new double[(int) maxLength];

        for(double i = 0; i < maxLength; i++){
            double TP = 0;
            double FP = 0;
            for(double[] d : REPORT){
                if(d[2] > i){
                    if(d[4] == 1)
                        TP++;
                    else
                        FP++;
                }
            }
            TPR[(int) i] = TP/P;
            FPR[(int) i] = FP/N;
        }

        double[] TPR2 = new double[(int) maxLength];
        double[] FPR2 = new double[(int) maxLength];

        for(double i = 0; i < maxMM; i++){
            double TP = 0;
            double FP = 0;
            for(double[] d : REPORT){
                if(d[3] > i){
                    if(d[4] == 1)
                        TP++;
                    else
                        FP++;
                }
            }
            TPR2[(int) i] = TP/P;
            FPR2[(int) i] = FP/N;
        }

        p = new Plot("ROC.jpeg", "FPR", "TPR");
        p.setColor(Color.RED);
        p.addPoints(FPR, TPR, 2);
        p.draw();
        p.setColor(Color.GREEN);
        p.addPoints(FPR2, TPR2, 2);
        p.setLimits(0.0, 1.0, 0.0, 1.0);

        File output = new File("src/main/resources/ROC.jpeg");
        ImageIO.write(p.getProcessor().getBufferedImage(), "jpeg", output);

        p.setLimits(0.0, 0.2, 0.5, 1.0);

        File output2 = new File("src/main/resources/ROC_Zoomed.jpeg");
        ImageIO.write(p.getProcessor().getBufferedImage(), "jpeg", output2);
    }
}
