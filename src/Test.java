public class Test {



    public static void main(String[] args) {
        DataSet dataSet = new DataSet(25, 1 / 5.316);
        DataSet dataSet2 = new DataSet(25, 1 / 5.316);

        dataSet.readFileAsPoints("SIVA_SREEGANESH.csv");
        dataSet2.readFileAsPoints("DOBERVICH_DAVID.csv");

        System.out.println(dataSet.getTotalDistance(50,65));
        System.out.println(dataSet2.getTotalDistance(50,65));


        System.out.println(dataSet.getAverageSpeed(30,40));
        System.out.println(dataSet2.getAverageSpeed(30,40));



        dataSet.addROI(new CircleROI(308,233, 100));
        dataSet2.addROI(new CircleROI(308,233, 100));

        System.out.println(dataSet.getAmountOfTimeInROI());
        System.out.println(dataSet2.getAmountOfTimeInROI());


    }

}
