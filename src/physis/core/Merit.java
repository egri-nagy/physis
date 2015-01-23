/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Merit.java,v 1.6 2000/06/30 16:23:33 sirna Exp $
 * $Revision: 1.6 $
 * $Date: 2000/06/30 16:23:33 $
 */
package physis.core;

/**
 * It's the merit of the digital organism. It's earned by counting the executed or copied instructions and multiplied this number with the bonuses.
 * <br>
 * CRC
 * <br>
 * 1. Storing the organism's merit value.
 * <br>
 * 2. Calculating the fitness value with as a function of gestation time.
 */
public class Merit {
   
    private double bonusmultiplier = 1;
    private int effectivelength;
    
    /**
     * Fitness is calculated by the merit divided by the gestation time.
     */
    public double calculateFitness(int gestation_time) {
        return (effectivelength * bonusmultiplier) / gestation_time;
    }
    
    /**
     * Merit equals the effective length multipled by the bonuses.
     */
    public int getMeritValue() {
        return (int)(effectivelength * bonusmultiplier);
    }
    
    /**
     *  Sets the effective length. After updates it should be updated and when proliferating.
     */
    public void setEffectiveLength(int effective_length){
        effectivelength = effective_length;
    }
    
    public int getEffectiveLength(){
        return effectivelength;
    }
    
    
    /**
     *  Sets the bonus multiplier.
     */
    public void setBonusMultiplier(double bonus_multiplier){
        bonusmultiplier = bonus_multiplier;
    }
    /**
     *  Multiplies bonus multiplier. It should happen only after task completition.
     */
    public void multiplyBonus(double bonus_multiplier){
        bonusmultiplier *= bonus_multiplier;
    }
    
    public double getBonusMultiplier(){
        return bonusmultiplier;
    }
    
    
}

