package physis.core.lifespace;

import java.io.Serializable;

public    class PositionInfo2DLattice implements Serializable{
    int x,y;
    PositionInfo2DLattice(int x_,int y_) { x = x_; y = y_;}
    
    public String toString(){ return "x: " + x + " y: " + y;}
}

