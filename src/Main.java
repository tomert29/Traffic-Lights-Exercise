import javax.swing.JRadioButton;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created on Mimuna 5767  upDate on Addar 5772 
 */

/**
 * @author �����
 */
public class Main {

    public static void main(String[] args) {

        final int numOfLights = 4 + 12 + 1, numOfCarsLights = 4, numOfWalkersLights = 12;
        StreetLight lights[] = new StreetLight[numOfLights];
        CarsLight carsLight[] = new CarsLight[numOfCarsLights];
        //YA cars light
        lights[0] = new StreetLight(3, 40, 430, 110, 472, 110, 514, 110);
        //Farbstein cars
        lights[1] = new StreetLight(3, 40, 450, 310, 450, 352, 450, 394);
        //KN side cars light
        lights[2] = new StreetLight(3, 40, 310, 630, 280, 605, 250, 580);
        //KN cars light
        lights[3] = new StreetLight(3, 40, 350, 350, 308, 350, 266, 350);


        lights[4] = new StreetLight(2, 20, 600, 18, 600, 40);
        lights[5] = new StreetLight(2, 20, 600, 227, 600, 205);
        lights[6] = new StreetLight(2, 20, 600, 255, 600, 277);
        lights[7] = new StreetLight(2, 20, 600, 455, 600, 433);
        lights[8] = new StreetLight(2, 20, 575, 475, 553, 475);
        lights[9] = new StreetLight(2, 20, 140, 608, 150, 590);
        lights[10] = new StreetLight(2, 20, 205, 475, 193, 490);
        lights[11] = new StreetLight(2, 20, 230, 475, 250, 475);
        lights[12] = new StreetLight(2, 20, 200, 453, 200, 433);
        lights[13] = new StreetLight(2, 20, 200, 255, 200, 277);
        lights[14] = new StreetLight(2, 20, 200, 227, 200, 205);
        lights[15] = new StreetLight(2, 20, 200, 18, 200, 40);

        lights[16] = new StreetLight(1, 30, 555, 645);

        TrafficLightFrame tlf = new TrafficLightFrame(" ���''� installation of traffic lights", lights);

        carsLight[0] = new CarsLight("YA cars light", lights[0], tlf.myPanel, 1, Arrays.asList(
                new WalkersLight(lights[4], tlf.myPanel),
                new WalkersLight(lights[5], tlf.myPanel)
        ));

        carsLight[1] = new CarsLight("Farbstein Cars Light", lights[1], tlf.myPanel, 2);
        carsLight[2] = new CarsLight("KN side light", lights[2], tlf.myPanel, 3, Arrays.asList(
                new WalkersLight(lights[9], tlf.myPanel),
                new WalkersLight(lights[10], tlf.myPanel)
        ));
        carsLight[3] = new CarsLight("KN cars light", lights[3], tlf.myPanel, 4, Arrays.asList(
                new WalkersLight(lights[6], tlf.myPanel),
                new WalkersLight(lights[7], tlf.myPanel),
                new WalkersLight(lights[12], tlf.myPanel),
                new WalkersLight(lights[13], tlf.myPanel)
        ));

        new MainStatechart(carsLight[0], carsLight[1], carsLight[2], carsLight[3], Arrays.asList(
                new WalkersLight(lights[8], tlf.myPanel),
                new WalkersLight(lights[11], tlf.myPanel),
                new WalkersLight(lights[14], tlf.myPanel),
                new WalkersLight(lights[15], tlf.myPanel)

        )).start();
        new BlinkingLight(lights[16], tlf.myPanel).start();

        MyActionListener myListener = new MyActionListener();

        JRadioButton butt[] = new JRadioButton[13];

        for (int i = 0; i < butt.length - 1; i++) {
            butt[i] = new JRadioButton();
            butt[i].setName(Integer.toString(i + 4));
            butt[i].setOpaque(false);
            butt[i].addActionListener(myListener);
            tlf.myPanel.add(butt[i]);
        }
        butt[0].setBounds(620, 30, 18, 18);
        butt[1].setBounds(620, 218, 18, 18);
        butt[2].setBounds(620, 267, 18, 18);
        butt[3].setBounds(620, 447, 18, 18);
        butt[4].setBounds(566, 495, 18, 18);
        butt[5].setBounds(162, 608, 18, 18);
        butt[6].setBounds(213, 495, 18, 18);
        butt[7].setBounds(240, 457, 18, 18);
        butt[8].setBounds(220, 443, 18, 18);
        butt[9].setBounds(220, 267, 18, 18);
        butt[10].setBounds(220, 218, 18, 18);
        butt[11].setBounds(220, 30, 18, 18);

        butt[12] = new JRadioButton();
        butt[12].setName(Integer.toString(16));
        butt[12].setBounds(50, 30, 55, 20);
        butt[12].setText("Shabat");
        butt[12].setOpaque(false);
        butt[12].addActionListener(myListener);
        tlf.myPanel.add(butt[12]);
    }
}
