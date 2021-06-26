import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class Panel extends JPanel {

        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;

        private int refresh = 144;

        private Timer timer;
        private ArrayList<Part> snake;
        private int speed;
        private int score;
        private int highScore;
        StringBuilder sb;
        private int length;
        boolean eatenApple;
        boolean alive;
        boolean beatScore;
        boolean increasedScore;
        boolean appleMap;
        boolean lost;
        Apple apple;

        private Image background;

        private Color snakeColor = Color.GREEN;
        boolean multiColor = false;



        public Panel(int width, int height) {
            try {
                background = ImageIO.read(new File("C:/Users/baller/cs/java/snake/res/back.jpg"));
            }catch (Exception e){e.printStackTrace();}
            setBounds(0, 0, width, height);
            setupKeyListener();
            speed = 5;

            //TODO: progressively ramp up speed based up on score

            timer = new Timer(1000/refresh, e->update()); //
            timer.start();
            length = 20;
            restart();
        }

        public void restart(){
            System.out.println("NEW GAME");
            snake = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                snake.add(new Part(getWidth()/2 + (speed*i), getHeight()-200, speed));
            }
            alive = true;
            eatenApple = false;
            increasedScore = true;
            appleMap = true;
            apple = applePlace();
            sb = new StringBuilder();
            score = 0;
            lost = false;
            beatScore = false;
        }

        public void addLength(ArrayList<Part> snake, Part back){
            Part newBack = new Part(back.getX(), back.getY(), back.getSpeed());
            snake.add(0, newBack);
        }

        public boolean eaten(Rectangle snake, Rectangle apple){
            if(snake.intersects(apple)){
                return true;
            }
            else{
                return false;
            }
        }

        public Apple applePlace(){
            int x = (int) (Math.random() * (getWidth() - 100) + 10);
            int y = (int) (Math.random() * (getHeight() - 100) + 10);

            return new Apple(x, y);
        }

        public void increaseScore(){
            score ++;
        }

        public void move(ArrayList<Part> snake, Part front){

            int direction = front.getDirection();

            if(direction == UP) {
                Part newFront = new Part(front.getX(), front.getY() - front.getSpeed(), front.getSpeed());
                newFront.setDirection(UP);
                snake.add(newFront);
            }

            if(direction == RIGHT){
                Part newFront = new Part(front.getX() + front.getSpeed(), front.getY(), front.getSpeed());
                newFront.setDirection(RIGHT);
                snake.add(newFront);
            }

            if(direction == LEFT){
                Part newFront = new Part(front.getX() - front.getSpeed(), front.getY(), front.getSpeed());
                newFront.setDirection(LEFT);
                snake.add(newFront);
            }

            if(direction == DOWN){
                Part newFront = new Part(front.getX(), front.getY() + front.getSpeed(), front.getSpeed());
                newFront.setDirection(DOWN);
                snake.add(newFront);
            }
        }

        public void lose(Part front){
            //    top                        right edge                            left edge                          bottom
            if(front.getY() < 0 || front.getX() + front.getW() > getWidth() || front.getX() < 0 || front.getY() + front.getH() > getHeight()){
                lost = true;
            }

            for (int i = 0; i < snake.size() - 15; i++) {
                if(front.getHitBox().intersects(snake.get(i).getHitBox())){
                    lost = true;
                }
            }
        }

        public void update(){

            Part front = snake.get(snake.size() - 1);
            Part back = snake.remove(0);

            if(eaten(front.getHitBox(), apple.getHitBox())){
                appleMap = false;
                eatenApple = true;
                apple = applePlace();
                appleMap = true;
            }

            move(snake, front);

            if(eatenApple){
                increaseScore();
                if(score > highScore) {
                    highScore = score;
                    beatScore = true;
                }

                for (int i = 0; i < 3; i++) {
                    addLength(snake, back);
                    repaint();
                }
                eatenApple = false;
            }

            lose(front);

            if(alive){
                repaint();
            }

        }


        public void setupKeyListener(){
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    sb.append(e.getKeyChar());
//                    snake.get(snake.size() - 1).typed(e.getKeyCode());
                    if(sb.toString().contains("game")){
                        if(alive){
                            System.out.println("You ended the game");
                            if(beatScore)
                                System.out.println("New high score!");
                            System.out.println("Final score: " + score);
                        }
                        restart();
                    }
                    else if(sb.toString().contains("multi")){
                        multiColor = true;
                    }
                    else if(sb.toString().contains("classic")){
                        snakeColor = Color.GREEN;
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    snake.get(snake.size() - 1).pressed(e.getKeyCode());
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        sb = new StringBuilder();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(background, 0,0, null);
            Graphics2D g2 = (Graphics2D) g;
            setBackground(Color.BLACK);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2.drawString("High score: " + highScore, 7, 20);
            g2.drawString("Score: " + score, 7, 45);
            if(multiColor)
                snakeColor = new Color((int)(Math.random() * 0x1000000));
            g2.setColor(snakeColor);
            for (Part part : snake) {
                part.draw(g2);
            }
            if(appleMap) {
                while(apple.getX() < 50 && apple.getY() < 20){
                    apple = applePlace();
                }
                for (int i = 0; i < snake.size() - 15; i++) {
                    if(snake.get(i).getHitBox().intersects(apple.getHitBox())){
                        apple = applePlace();
                    } else {
                        apple.draw(g2);
                    }
                }
            }

            if(lost) {
                g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
                g2.drawString("GAME OVER", getWidth()/2 - 150, getHeight()/2);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("TimesRoman", Font.PLAIN, 10));
                g2.drawString("type \"game\" to start a new game", getWidth()/2 - 75, getHeight()/2 + 30);
                System.out.println("Game Over!");
                if(beatScore)
                    System.out.println("NEW HIGH SCORE");
                System.out.println("Final score: " + score);
                alive = false;
            }


        }


}
