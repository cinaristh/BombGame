import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

//for the timer...
import javax.swing.Timer;

//for the sounds...
import java.io.File;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main {

    //private attributes
    private JFrame frame1;
    private JPanel panel1;

    private JLabel image1;
    private int tick_count;
    private int guess;
    private int rightAnswer;
    private Boolean started;
    private Boolean exploded;
    private JButton button1;


    private JButton resetButton;
    private JCheckBox hintButton;

    private JCheckBox cheatButton;

    private JTextField codeTextField;
    private JLabel timeLeftLabel;
    private JLabel label1;

    private JLabel label2;

    private  Timer timer1;

    private int rand;


    //public properties
    public void setRightAnswer(int val) {
        rightAnswer = val;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    //instantiate Main class...
    public static void main(String[] args) {
        //new version main form
        new Main();
    }

//working with images https://www.youtube.com/watch?v=hSBalmWkWj8

    //constructor
    public Main() {

        frame1 = new JFrame("Bomb Game");
        frame1.setBounds(0, 0, 1000, 700);

        frame1.setMinimumSize(new Dimension(1000, 700));
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setResizable(true);

        panel1 = new JPanel();
        image1 = new JLabel();
        button1 = new JButton("Start");
        resetButton = new JButton("Reset");
        codeTextField = new JTextField();
        timeLeftLabel = new JLabel("Time left: 30");
        label1 = new JLabel("Welcome to the Bomb Game");
        label2 = new JLabel("Enter a number");
        hintButton = new JCheckBox("hint");
        cheatButton = new JCheckBox("cheat");

        panel1.setLayout(null);
        panel1.setBounds(0, 0, 1000, 700);
        panel1.setBackground(Color.black);

        image1.setBounds(400, 70, 300, 216);
        image1.setIcon(new ImageIcon("bomb_anim.gif"));

        label1.setBounds(50, 10, 300, 25);
        label1.setForeground(Color.white);

        label2.setBounds(40,350,300,25);
        label2.setForeground(Color.GREEN);
        label2.setFont(new Font( "Arial", Font.PLAIN,18));


        button1.setBounds(50, 150, 75, 25);

        resetButton.setBounds(50, 230, 75, 25);
        resetButton.setVisible(false);

        hintButton.setBounds(150, 150, 55, 25);
        hintButton.setVisible(false);

        cheatButton.setBounds(50, 400,70,25 );
        cheatButton.setVisible(false);


        codeTextField.setBounds(70, 100, 30, 30);
        codeTextField.setFont(new Font("Serif", Font.BOLD, 24));
        codeTextField.setText("00");
        codeTextField.setEnabled(false);



        timeLeftLabel.setFont(new Font("Serif", Font.BOLD, 30));
        timeLeftLabel.setBounds(50, 50, 300, 34);
        timeLeftLabel.setForeground(Color.white);


        panel1.add(image1);
        panel1.add(label1);
        panel1.add(label2);
        panel1.add(button1);
        panel1.add(codeTextField);
        panel1.add(timeLeftLabel);
        panel1.add(resetButton);
        panel1.add(hintButton);
        panel1.add(cheatButton);

        frame1.add(panel1);

        frame1.setVisible(true);

        tick_count = 30;

        started = false;

        codeTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    guess = Integer.parseInt(codeTextField.getText());
                    guess();
                    codeTextField.setText("");
                } catch (NumberFormatException ex) {
                    label1.setText("Numbers only");
                }
            }
        });


        timer1 = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (started == true && tick_count > 0) {
                    tick_count = tick_count - 1;
                    timeLeftLabel.setText("Time left: " + Integer.toString(tick_count));

                    if (tick_count > 27) {
                        image1.setIcon(new ImageIcon("bomb_anim.gif"));
                    } else if (tick_count > 22) {
                        image1.setIcon(new ImageIcon("bomb_anim2.gif"));
                    } else if (tick_count > 15) {
                        image1.setIcon(new ImageIcon("bomb_anim3.gif"));
                    } else if (tick_count > 8) {
                        image1.setIcon(new ImageIcon("bomb_anim4.gif"));
                    }
                }
                if (tick_count == 0) {
                    //explode();
                    image1.setIcon(null);
                    image1.setIcon(new ImageIcon("explosion2.gif"));

                    image1.setBounds(400, 70, 600, 400);

                    if (exploded == false) {
                        playSound();
                        exploded = true;
                    }


                    image1.setIcon(new ImageIcon("black.gif"));

                }
            }

        });


        //start button...
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRightAnswerCode();
                exploded = false;
                codeTextField.setEnabled(true);
                button1.setEnabled(false);
                resetButton.setEnabled(true);
                resetButton.setVisible(true);
                hintButton.setVisible(true);
                cheatButton.setVisible(true);
                timer1.start();
                started = true;
                guess();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRightAnswerCode();
                guess();

                Timer timer = new Timer(3000, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        image1.setIcon(new ImageIcon("bomb_anim.gif"));

                    }
                });


                timer.setRepeats(false);
                timer.start();
                codeTextField.setEnabled(false);
                resetButton.setVisible(false);
                button1.setEnabled(true);
                started = false;
                tick_count = 30;
                timeLeftLabel.setText("Time left: " + Integer.toString(tick_count));
                cheatButton.setSelected(false);

            }
        });
//hint button
        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hintButton.isSelected()) {
                    // Code to execute when the checkbox is selected
                    hint();
                }else{
                    label1.setText("");



            }
        }});




    }    //end of constructor

    public void guess() {
        if (guess == getRightAnswer()) {
            image1.setIcon(new ImageIcon("bomb_diffused.gif"));
            timer1.stop();
        }
        else if (guess < getRightAnswer()&& guess!= 00) {
            label2.setText(guess + " is too low");
        } else if (guess > getRightAnswer()) {
            label2.setText(guess + " is too high");
        }
    }


    public void setRightAnswerCode () {
        int rand = Math.toIntExact(Math.round(Math.random() * 99));
        if (rand > 99) {
            rand = 99;
        }
        if (rand < 1) {
            rand = 1;
        }
        setRightAnswer(rand);
        //cheatButton
        int finalRand = rand;
        cheatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cheatButton.isSelected()){
                    frame1.setTitle(String.valueOf(rightAnswer));

                }else{
                    frame1.setTitle("??");
                }
            }});
    }


    public void hint () {
        if (getRightAnswer() % 2 == 0) {
            label1.setText("Number is even");
        } else {
            label1.setText("Number is odd");
        }
    }

    public void playSound () {

        File soundFile = new File("explosion.wav");

        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));
            clip.start();
        } catch (Exception e) {

        }

    }

}