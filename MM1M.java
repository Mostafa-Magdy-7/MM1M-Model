package com.example.mm1m;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.*;

public class MM1M extends Application {
    Label resultLabel = new Label("Waiting...");
    Label Check = new Label("Please insert the numbers in input");
    Label rules= new Label("Please insert to calculate the Rules");
    Label pn= new Label("Fill the inputs To see teh Probabilities ");
    List<Circle> customerQueue = new ArrayList<>();


    Line verticalLine = new Line();
    @Override

    public void start(Stage stage) throws IOException {

        BorderPane root = new BorderPane();


        VBox rightBox = new VBox(10);

      Label l=new Label("Lambda(Arrival Rate)");
      TextField lambda=new TextField();



      Label M=new Label("Mue  (Service Rate)");
      TextField mue=new TextField();


        Label Q=new Label("Queue");
      TextField queue=new TextField();



        Button start=new Button("Start");
        start.setOnAction(e -> {
            try {
                float lam = Float.parseFloat(lambda.getText());
                float mu = Float.parseFloat(mue.getText());
                int q = Integer.parseInt(queue.getText());

                intconverter(lam, mu, q); // passing correct types now
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input! Please enter valid numbers.");
                Check.setText("Invalid input! Please enter valid numbers.");

            }
        });

      Button calculate=new Button("Calculate ");
      calculate.setOnAction(e->{
          try {
              float lam = Float.parseFloat(lambda.getText());
              float mu = Float.parseFloat(mue.getText());
              int q = Integer.parseInt(queue.getText());

              rules(lam, mu, q); // passing correct types now
          } catch (NumberFormatException ex) {
              System.out.println("Invalid input! Please enter valid numbers.");
              Check.setText("Invalid input! Please enter valid numbers.");

          }
      });
      Button Visual =new Button("Visualize");
      Visual.setOnAction(e->{
          try{
              float lam = Float.parseFloat(lambda.getText());
              float mu = Float.parseFloat(mue.getText());
              int q = Integer.parseInt(queue.getText());

                 simulateArrivals(lam, q, root,mu);


          }
          catch (NumberFormatException ex){
              Check.setText("Error happens while Visualizing");

          }
      });
      rightBox.getChildren().addAll(l,lambda,M,mue,Q,queue,start,calculate,Check,resultLabel,rules,pn,Visual);
      VBox.setMargin(rightBox,new Insets(50,0,0,0));
      root.setRight(rightBox);
      root.getChildren().add(verticalLine);

        UI();
      rightBox.setPadding(new Insets(50,10,0,0));

        Scene scene = new Scene(root, 1000, 1300);
        stage.setScene(scene);
        stage.show();
    }
    private void rules(float lambda,float mue,int queue)
    {
        float p=lambda/mue;
        float P0= (float) ((1-p)/(1-Math.pow(p,queue+1)));
        int i;
        float sum=0;
        ArrayList<String> Probs = new ArrayList<String>();
        ArrayList<Float> Prob_n = new ArrayList<Float>();
        for (i=1;i<=queue;i++)
        {
         float Pn= (float) (Math.pow(p,i)*P0);
         Prob_n.add(Pn);
         Probs.add("the Porb of "+i+" = "+Pn+"\n");


        }
        float Pb=(float) (Math.pow(p,queue)*P0);
        float lameff=lambda*(1-Pb);
        int k;
        int L=0;
        for (k=1;k<=queue;k++)
        {
            sum+=(k*Prob_n.get(L));
        }
        float ts=sum/lameff;
        float tq=ts-(1/mue);
        System.out.println(Probs);
        pn.setText("The probability is = \n"+ Probs);
        rules.setText("Your P= "+p+"\n P0="+P0+"\n"+"Pb = "+Pb+"\n"+"Lambda Effective =  "+lameff+"\n"+"Avg number in the system = "+sum+"\n"+"Ts = "+ts+"\n"+"Tq = "+tq+"\n");

    }
    private void intconverter(float lambda,float mue,int queue)
    {

        System.out.println("You entered: " + lambda+mue+queue);
        resultLabel.setText("Your data \n Lambda = "+lambda+"\n Mue = "+mue+"\n Queue = "+queue+"\n");

    }
//    private void visual(float lambda,float mue,int queue)
//    {
//        int min = 0;
//        int max = (int) (lambda * 10); // cast to int
//
//        int randomValue = (int)(Math.random() * (max - min + 1)) + min;
//
//    }
    private void UI()
    {
        verticalLine.setStartX(0);
        verticalLine.setStartY(0);
        verticalLine.setEndX(0);
        verticalLine.setEndY(400);
        verticalLine.setStrokeWidth(2);
        verticalLine.setStyle("-fx-stroke: lightgray;");
    }



    private void simulateArrivals(float lambda, int queueLimit, Pane root,float mue) {
        new Thread(() -> {
            for (int i = 0; i < queueLimit; i++) {
                double interval = -Math.log(1 - Math.random()) / lambda; // in minutes

                try {
                    Thread.sleep((long) (interval * 60 * 1000)); // convert to ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int finalI = i;
                Platform.runLater(() -> {
                    double yPosition = 100 + finalI * 70;
                    Circle customer = new Circle(20);
                    customer.setFill(Color.RED);
                    customer.setStroke(Color.BLACK);
                    customer.setCenterX(300);
                    customer.setCenterY(yPosition); // Set it ONCE here

                    root.getChildren().add(customer);
                    customerQueue.add(customer);
                });
            }
        }).start();
        simulateService(mue, customerQueue, root);

    }
    private void simulateService(float mu, List<Circle> queue, Pane root) {
        new Thread(() -> {
            while (true) {
                if (!queue.isEmpty()) {
                    Circle current = queue.get(0);

                    // Change to blue on UI thread
                    Platform.runLater(() -> current.setFill(Color.BLUE));

                    double serviceTime = -Math.log(1 - Math.random()) / mu;
                    try {
                        Thread.sleep((long)(serviceTime * 60 * 1000)); // Convert to ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // First remove from the list in background (important!)
                    queue.remove(current);

                    // Now remove from UI in UI thread
                    Platform.runLater(() -> root.getChildren().remove(current));
                } else {
                    try {
                        Thread.sleep(500); // Small wait if queue is empty
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    public static void main(String[] args) {
        launch();
    }
}