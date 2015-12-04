package ad310.battleship.model;

public class LineSegment {
    public final Point Start;
    public final Point End;

    public LineSegment(Ship ship) {
        Start = new Point(ship.locations.get(0));
        int lastIndex = ship.locations.size() - 1;
        End =  new Point(ship.locations.get(lastIndex));
    }

    public boolean isIntersecting(LineSegment other) {
        if (this.isVertical() || other.isVertical()) {
            return false;
        }
        double slope1 = this.getSlope();
        double intercept1 = this.getIntercept();

        double slope2 = other.getSlope();
        double intercept2 = other.getIntercept();

        double intersectX = (intercept1 - intercept2) / (slope2 - slope1);
        double intersectY = (slope1 * intersectX) + intercept1;

        boolean xWithinFirst = this.isWithinXBounds(intersectX);
        boolean yWithinFirst = this.isWithinYBounds(intersectY);
        boolean xWithinSecond = other.isWithinXBounds(intersectX);
        boolean yWithinSecond = other.isWithinYBounds(intersectY);

        return xWithinFirst && yWithinFirst && xWithinSecond && yWithinSecond;
    }

    private boolean isWithinYBounds(double intersect) {
        double lower = Math.min(Start.Y, End.Y);
        double upper = Math.max(Start.Y, End.Y);

        return (intersect >= lower) && (intersect <= upper);
    }

    private boolean isWithinXBounds(double intersect) {
        double lower = Math.min(Start.X, End.X);
        double upper = Math.max(Start.X, End.X);

        return (intersect >= lower) && (intersect <= upper);
    }

    public double getSlope() {
        return (Start.Y - End.Y) / (Start.X - End.X);
    }

    public boolean isVertical() {
        return End.X == Start.X;
    }

    public double getIntercept() {
        return Start.Y - (getSlope() * Start.X);
    }
}
