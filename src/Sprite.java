import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    public Vector position = new Vector();
    public Vector velocity = new Vector();
    public double rotation; //degrees
    public Boundary boundary = new Boundary();
    public Image image;
    public boolean remove = true;
    public double aliveTime = 0;

    public Sprite(String imageFileName){
        setImage(imageFileName);
    }

    //constructor for laser
    public Sprite(Sprite spaceship, String source){
        image = new Image(source+"/laser.png");
        rotation = spaceship.rotation;
        position.set(spaceship.position.x,spaceship.position.y);
        velocity.setLength(500);
        velocity.setAngle(rotation);
        boundary.setSize(image.getWidth(),image.getWidth());
        aliveTime = 5;
        remove = false;
    }

    public void setImage(String imageFileName){
        image = new Image(imageFileName);
        boundary.setSize(image.getWidth(),image.getHeight());
    }

    public Boundary getBoundary(){
        this.boundary.setPosition(
                position.x - boundary.width/2,
                position.y - boundary.height/2);
        return this.boundary;
    }

    public void generate(double x, double y, double time){
        remove=false;
        position.set(x,y);
        aliveTime=time;
    }

    public boolean overlaps(Sprite other){
        return remove = getBoundary().overlaps(other.getBoundary());
    }

    public boolean isRemoved(){return (remove || aliveTime<=0);}

    public void wrap (double screenWidth, double screenHeight){

        double length = image.getHeight()/2;

        if(position.x + length < 0)
            position.x = screenWidth + length;
        if(position.x > screenWidth + length)
            position.x = -length / 2;
        if(position.y + length < 0)
            position.y = screenHeight + length;
        if(position.y > screenHeight + length)
            position.y = -length;
    }

    public void update(){
        // update position according to velocity
        double deltaTime = 1/60.0;
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        aliveTime-=deltaTime;
    }

    public void render(GraphicsContext context){
        context.save();
        context.translate(position.x,position.y);
        context.rotate(rotation);
        context.drawImage(image, -image.getWidth()/2, -image.getHeight()/2);
        context.restore();
    }
}