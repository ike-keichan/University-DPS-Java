package countdown;

/********************************************************/
// j.n.magee 16.4.98
// j.n.magee revised 8.8.2004


import java.awt.*;
//import java.applet.*;
import java.awt.event.*;

public class NumberCanvas extends Canvas {
    int value_ = 0;
    String title_;

    Font f1 = new Font("Helvetica",Font.BOLD,36);
    Font f2 = new Font("Times",Font.ITALIC+Font.BOLD,24);

	CountDown c = null;

    public NumberCanvas(String title) {
    	this(title,Color.cyan);
    }
    public NumberCanvas(String title, CountDown c) {
        this(title);
		this.c = c;
    }

    public NumberCanvas(String title,Color c) {
        super();
        title_=title;
        setBackground(c);

	}

    public void setcolor(Color c){
        setBackground(c);
        repaint();
    }

    public void setValue(int newval){
        value_ = newval;
        repaint();
    }

    private int getValue(){
    	return value_;
    }

    public void incrementValue(){
    	value_++;
    	repaint();
    }

    public void decrementValue(){
    	if(getValue() > 0) {
    		value_--;
    		repaint();
    	}
    }

    public void paint(Graphics g){

            g.setColor(Color.black);

         // Display the title
            g.setFont(f2);
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(title_);
            int h = fm.getHeight();
            int x = (getSize().width - w)/2;
            int y = h;
            g.drawString(title_, x, y);
            g.drawLine(x,y+3,x+w,y+3);


         // Display the value
            g.setFont(f1);
            fm = g.getFontMetrics();
            String s1 = String.valueOf(value_);
            w = fm.stringWidth(s1);
            h = fm.getHeight();
            x = (getSize().width - w)/2;
            y = (getSize().height+ h)/2;
            g.drawString(s1, x, y);
         }

}