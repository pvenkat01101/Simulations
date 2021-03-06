// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.eatingandexercise.model;

import java.util.ArrayList;
import java.util.Random;

import edu.colorado.phet.common.motion.model.DefaultTemporalVariable;
import edu.colorado.phet.common.motion.model.ITemporalVariable;
import edu.colorado.phet.common.motion.model.IVariable;
import edu.colorado.phet.common.phetcommon.math.Function;
import edu.colorado.phet.common.phetcommon.math.MathUtil;
import edu.colorado.phet.eatingandexercise.EatingAndExerciseResources;
import edu.colorado.phet.eatingandexercise.control.Activity;
import edu.colorado.phet.eatingandexercise.control.CaloricItem;
import edu.colorado.phet.eatingandexercise.module.eatingandexercise.CaloricFoodItem;
import edu.colorado.phet.eatingandexercise.module.eatingandexercise.EatingAndExerciseModel;
import edu.colorado.phet.eatingandexercise.module.eatingandexercise.FoodCalorieSet;

/**
 * Created by: Sam
 * Apr 3, 2008 at 1:05:20 PM
 */
public class Human {

    private String name;
    private ArrayList listeners = new ArrayList();

    private Gender gender = DEFAULT_VALUE.getGender();
    private DefaultTemporalVariable height = new DefaultTemporalVariable();//meters
    private DefaultTemporalVariable mass = new DefaultTemporalVariable();//kg
    private DefaultTemporalVariable age = new DefaultTemporalVariable();//sec
    private DefaultTemporalVariable fatMassFraction = new DefaultTemporalVariable();

    private DefaultTemporalVariable lipids = new DefaultTemporalVariable();
    private DefaultTemporalVariable carbs = new DefaultTemporalVariable();
    private DefaultTemporalVariable proteins = new DefaultTemporalVariable();

    private DefaultTemporalVariable activity = new DefaultTemporalVariable();//initialized to 0.5*BMR
    private DefaultTemporalVariable exercise = new DefaultTemporalVariable();//initialized to make sure weight is constant at startup
    private DefaultTemporalVariable bmr = new DefaultTemporalVariable();//dependent variable
    //    private Exercise exerciseObject = null;
    //New defaults: 5'8" 150 lbs 22 years
    private static final ReferenceHuman REFERENCE_MALE = new ReferenceHuman( true, 22, 5 + 8 / 12.0, EatingAndExerciseUnits.poundsToKg( 150 ), 86 );
    private static final ReferenceHuman REFERENCE_FEMALE = new ReferenceHuman( false, 22, 5 + 5 / 12.0, EatingAndExerciseUnits.poundsToKg( 135 ), 74 );
    public static final ReferenceHuman DEFAULT_VALUE = REFERENCE_FEMALE;

    private CalorieSet exerciseItems = new CalorieSet();
    private FoodCalorieSet foodItems = new FoodCalorieSet();
    private double activityLevel = Activity.DEFAULT_ACTIVITY_LEVEL.getValue();
    private ITemporalVariable caloricIntakeVariable = new DefaultTemporalVariable();
    private ITemporalVariable caloricBurnVariable = new DefaultTemporalVariable();
    private CaloricFoodItem defaultIntake;
    public static final String FOOD_PYRAMID = "food-pyramid.png";

    //alive, starvation and heart attacks
    private boolean alive = true;
    private double starvingTime = 0;
    private Random random = new Random();
    private int heartAttacks = 0;
    private Activity activityObject;

    //health monitors
    private double heartStrain;
    private double heartStrength;

    private HumanUpdate humanUpdate = new DefaultHumanUpdate();

    public Human() {
        addListener( new Adapter() {
            public void heartHealthChanged() {
                notifyHeartAttackProbabilityChanged();
            }
        } );
        lipids.addListener( new IVariable.Listener() {
            public void valueChanged() {
                notifyDietChanged();
            }
        } );
        carbs.addListener( new IVariable.Listener() {
            public void valueChanged() {
                notifyDietChanged();
            }
        } );
        proteins.addListener( new IVariable.Listener() {
            public void valueChanged() {
                notifyDietChanged();
            }
        } );
        exercise.addListener( new IVariable.Listener() {
            public void valueChanged() {
                notifyExerciseChanged();
            }
        } );
        exerciseItems.addListener( new CalorieSet.Listener() {
            public void itemAdded( CaloricItem item ) {
                updateExercise();
            }

            public void itemRemoved( CaloricItem item ) {
                updateExercise();
            }

            public void itemChanged( CaloricItem item ) {
                updateExercise();
            }
        } );
        foodItems.addListener( new CalorieSet.Listener() {
            public void itemAdded( CaloricItem item ) {
                updateIntake();
            }

            public void itemRemoved( CaloricItem item ) {
                updateIntake();
            }

            public void itemChanged( CaloricItem item ) {
                updateIntake();
            }

        } );
        resetAll();
    }

    public void resetAll() {
        name = "Larry";

        clearTemporalVariables();

        setGender( DEFAULT_VALUE.getGender() );
        double heightMeters = DEFAULT_VALUE.getHeightMeters();
//        System.out.println( "heightMeters = " + heightMeters );
//        System.out.println( "heightFt=" + metersToFeetStr( heightMeters ) );
        setHeight( heightMeters );
        setMass( DEFAULT_VALUE.getMassKG() );
        setAge( DEFAULT_VALUE.getAgeSeconds() );
        setFatMassPercent( ( 100 - DEFAULT_VALUE.getFatFreeMassPercent() ) );


        setActivityLevel( Activity.DEFAULT_ACTIVITY_LEVELS[1] );
        Diet initialDiet = EatingAndExerciseModel.BALANCED_DIET;
        foodItems.clear();
        exerciseItems.clear();
        if ( defaultIntake == null ) {//todo: change to single instance so that view/controller can observe it
//            defaultIntake = new CaloricFoodItem( EatingAndExerciseResources.getString( "diet.healthy" ), FOOD_PYRAMID, 0.3 * 9 * 2000 / 5.5 / 9, 0.4 * 4 * 2000 / 5.5 / 4, 0.3 * 4 * 2000 / 5.5 / 4, false );
            defaultIntake = new CaloricFoodItem( EatingAndExerciseResources.getString( "diet.healthy" ), FOOD_PYRAMID, initialDiet.getFat() / 9, initialDiet.getCarb() / 4, initialDiet.getProtein() / 4, false );
        }
//        foodItems.addItem( defaultIntake );//todo: standardize constructor units
        updateIntake();

        //todo remove the need for this workaround
        simulationTimeChanged( 0.0 );
        foodItems.addListener( new CalorieSet.Listener() {
            public void itemAdded( CaloricItem item ) {
                notifyDietChanged();
            }

            public void itemRemoved( CaloricItem item ) {
                notifyDietChanged();
            }

            public void itemChanged( CaloricItem item ) {
                notifyDietChanged();
            }
        } );
        exerciseItems.addListener( new CalorieSet.Listener() {
            public void itemAdded( CaloricItem item ) {
                exercise.setValue( exerciseItems.getTotalCalories() );
                notifyExerciseChanged();
            }

            public void itemRemoved( CaloricItem item ) {
                exercise.setValue( exerciseItems.getTotalCalories() );
                notifyExerciseChanged();
            }

            public void itemChanged( CaloricItem item ) {
                exercise.setValue( exerciseItems.getTotalCalories() );
                notifyExerciseChanged();
            }
        } );
        heartAttacks = 0;
        setAlive( true );
        starvingTime = 0;

        //need some data in the exercise and fat % for updating the health indicators
        //so add some additional data to the exercise series so that the initial reading
        //for heart strength will be higher, as if we used to exercise daily before the sim started
        //NP - changed exercise initial value to make heart strength about 1/2 way up to start
        for ( int i = 1; i < 100; i++ ) {
            exercise.addValue( 150, getAge() - EatingAndExerciseUnits.daysToSeconds( i ) );
            fatMassFraction.addValue( fatMassFraction.getValue(), getAge() - EatingAndExerciseUnits.daysToSeconds( i ) );
        }
        exercise.addValue( 0, getAge() );

        updateHealthIndicators();
        updateBMR();
    }

    public double getActivityLevel() {
        return activityLevel;
    }

    public ITemporalVariable getMassVariable() {
        return mass;
    }

    public ITemporalVariable getCaloricIntakeVariable() {
        return caloricIntakeVariable;
    }

    public ITemporalVariable getCaloricBurnVariable() {
        return caloricBurnVariable;
    }

    public CaloricFoodItem getDefaultIntake() {
        return defaultIntake;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive( boolean alive ) {
        if ( alive != this.alive ) {
            this.alive = alive;
            notifyAliveChanged();
        }
    }

    private void notifyAliveChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).aliveChanged();
        }
    }

    private void clearTemporalVariables() {
        height.clear();
        mass.clear();
        age.clear();
        fatMassFraction.clear();
        lipids.clear();
        carbs.clear();
        proteins.clear();
        activity.clear();
        exercise.clear();
        bmr.clear();
        caloricIntakeVariable.clear();
        caloricBurnVariable.clear();
    }

    private void updateIntake() {
        lipids.setValue( foodItems.getTotalLipidCalories() );
        carbs.setValue( foodItems.getTotalCarbCalories() );
        proteins.setValue( foodItems.getTotalProteinCalories() );
    }

    private void updateExercise() {
        exercise.setValue( exerciseItems.getTotalCalories() );
    }

    public CalorieSet getSelectedFoods() {
        return foodItems;
    }

    public CalorieSet getSelectedExercise() {
        return exerciseItems;
    }

    private void notifyExerciseChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).exerciseChanged();
        }
        notifyCaloricBurnChanged();
    }

    public Diet getDiet() {
        return EatingAndExerciseModel.getDiet( lipids.getValue(), carbs.getValue(), proteins.getValue() );
    }

    private void notifyDietChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).dietChanged();
            ( (Listener) listeners.get( i ) ).caloricIntakeChanged();
        }
    }

    /*
     * SR - According to Exercise Physiology 4th Ed. By McArdle, Katch & Katch, 1996, p154, the Resting Daily Energy Expenditure is:
     RDEE (kcal) =370+21.6 * FFM (kg)
     This equation is said to apply to males and females over a wide range of body weights, and is the equation recommended to us by the CU Integrated Physiologists
     */
    private void updateBMR() {
        bmr.setValue( 392 + 21.8 * getLeanBodyMass() );
        updateActivity();
    }

    public double getFatMass() {
        return getFatMassPercent() / 100.0 * getMass();
    }

    public double getLeanBodyMass() {
        return getFatFreeMassPercent() / 100.0 * getMass();
    }

    /**
     * http://usmilitary.about.com/od/airforcejoin/a/afmaxweight.htm
     * The formula to compute BMI is
     * weight (in pounds) divided by the square of height (in inches),
     * multiplied by 704.5
     * <p/>
     * (Don't worry about that though, the below chart shows the maximum and minimum weights using the formula).
     *
     * @return
     */
    public double getBMIOrig() {
        return getWeightPounds() / Math.pow( getHeightInches(), 2 ) * 704.5;
    }

    public double getBMI() {
        return getMass() / Math.pow( getHeight(), 2 );
    }

    private double getHeightInches() {
        return getHeight() / 0.0254;
    }

    private double getWeightPounds() {
        return getMass() * 2.20462262;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

//    public double getLeanMuscleMass() {
//        return leanMuscleMass.getValue();
//    }

//    public double getFatPercent() {
//        return 100 - leanMuscleMass.;
//    }

//    public void setLeanMuscleMass( double value ) {
//        this.leanMuscleMass.setValue( value );
//        notifyMusclePercentChanged();
//        notifyFatPercentChanged();
//    }

    private void notifyFatPercentChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).fatPercentChanged();
        }
    }

    private void notifyMusclePercentChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            Listener listener = (Listener) listeners.get( i );
            listener.musclePercentChanged();
        }
    }

//    public void setFatPercent( double value ) {
//        this.leanMuscleMass = 100 - value;
//        notifyFatPercentChanged();
//        notifyMusclePercentChanged();

    //    }


    public void setActivityLevel( Activity activity ) {
        if ( this.activityObject != activity ) {
            this.activityObject = activity;
            activityLevel = activity.getValue();
            updateActivity();
            notifyActivityLevelChanged();
        }
    }

    private void notifyActivityLevelChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).activityLevelChanged();
        }
    }

    private void updateActivity() {
        this.activity.setValue( getActivityCaloriesPerDay() );
        notifyActivityChanged();
    }

    public double getActivityCaloriesPerDay() {
        //this helps stabilize the model, and makes more sense to have activity depend on mass, rather than proportional to BMR as in previous model
        return activityLevel * getMass() * 20;
    }

    private void notifyActivityChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).activityChanged();
        }
        notifyCaloricBurnChanged();
    }

    private void notifyCaloricBurnChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).caloricBurnChanged();
        }
    }

    public DefaultTemporalVariable getLipids() {
        return lipids;
    }

    public DefaultTemporalVariable getCarbs() {
        return carbs;
    }

    public DefaultTemporalVariable getProteins() {
        return proteins;
    }

    public DefaultTemporalVariable getBmr() {
        return bmr;
    }

    public DefaultTemporalVariable getActivity() {
        return activity;
    }

    public DefaultTemporalVariable getExercise() {
        return exercise;
    }

    public void simulationTimeChanged( double simulationTimeChange ) {
        setAge( getAge() + simulationTimeChange );

        humanUpdate.update( this, simulationTimeChange );

        caloricIntakeVariable.setValue( getDailyCaloricIntake() );
        caloricIntakeVariable.addValue( getDailyCaloricIntake(), getAge() );

        caloricBurnVariable.setValue( getDailyCaloricBurn() );
        caloricBurnVariable.addValue( getDailyCaloricBurn(), getAge() );

        handleStarving( simulationTimeChange );
        handleHeartAttack( simulationTimeChange );

        updateHealthIndicators();
    }

    private void updateHealthIndicators() {
        exercise.addValue( exercise.getValue(), getAge() );
        activity.addValue( activity.getValue(), getAge() );
        fatMassFraction.addValue( fatMassFraction.getValue(), getAge() );
        //- Heart strength: this is a bar chart based a running average of exercise amount over the last X days (let's try X=30 days to start).
//NP will determine the range for heart strength based on exercise. For now, lets make it 250-1000 cal/day as the healthy range.
//    >>SR: Should this account for activity (lifestyle), or just exercise on top of that?

        double averageExercise = exercise.estimateAverage( getAge() - EatingAndExerciseUnits.daysToSeconds( 30 ), getAge() );
//        System.out.println( "averageExercise = " + averageExercise );
        double averageActivity = activity.estimateAverage( getAge() - EatingAndExerciseUnits.daysToSeconds( 30 ), getAge() );
        //NP 9-2-08 Changed activityToCount back to use a reduced fraction of averageActivity
        double activityToCount = Math.max( ( averageActivity - 200 ) * 0.2, 0 );
        if ( activityToCount > 100 ) {
            activityToCount = 100;  //maximum activity calories that contribute to heart strength
        }
//        System.out.println( "avgExercise=" + averageExercise + ", averageActivity=" + averageActivity + ", counting activity: " + activityToCount );

        double caloriesToConsiderForHeartStrength = averageExercise + activityToCount;
        double exercise_cal_max = 1100.0;
        double heartStrength = log10( 1 + 100 * caloriesToConsiderForHeartStrength / exercise_cal_max ) / log10( 101 );
//        System.out.println( "unclamped heartStrength = " + heartStrength );
        setHeartStrength( MathUtil.clamp( 0, heartStrength, 1.0 ) );

        double averagePercentFat = fatMassFraction.estimateAverage( getAge() - EatingAndExerciseUnits.daysToSeconds( 30 ), getAge() ) * 100;
//        System.out.println( "averagePercentFat = " + averagePercentFat );
        double distance = gender.getDistanceFromNormalRangeInPercent( averagePercentFat );
//        System.out.println( "averagePercentFat = " + averagePercentFat + ", distance=" + distance );
        //multiply by two so that 100% fat maps to 1.0 in strain
        setHeartStrain( MathUtil.clamp( 0, distance * 2 / 100.0, 1.0 ) );
    }

    private double log10( double v ) {
        return log( 10, v );
    }

    public static double log( double base, double x ) {
        return Math.log( x ) / Math.log( base );
    }

    public void setHeartStrength( double heartStrength ) {
        if ( heartStrength != this.heartStrength ) {
            this.heartStrength = heartStrength;
            notifyHeartStrengthChanged();
            notifyHeartHealthChanged();
        }
    }

    public void setHeartStrain( double heartStrain ) {
        if ( this.heartStrain != heartStrain ) {
            this.heartStrain = heartStrain;
            notifyHeartStrainChanged();
            notifyHeartHealthChanged();
        }
    }

    private void notifyHeartHealthChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).heartHealthChanged();
        }
    }

    private void notifyHeartStrainChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).heartStrainChanged();
        }
    }

    private void notifyHeartStrengthChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).heartStrengthChanged();
        }
    }

    private void handleHeartAttack( double simulationTimeChange ) {
        /*Model for heart attack:
        If you go above 25%/32% (men/women) fat, you begin to have a probability of heart attack each day, p_attack.
        Below these %fat thresholds, p_attack = 0.

        p_attack = p_0 * (%fat - %fat_0)

        ...where p_0 is a constant we adjust to make heart attack fairly likely
        (within a couple of years) for $fat > 50, %fat_0 = 25%/32% for men/women.
        */

        double heartAttackProbabilityPerDay = getHeartAttackProbabilityPerDay();
        double heartAttackProbabilityPerSec = heartAttackProbabilityPerDay / EatingAndExerciseUnits.daysToSeconds( 1 );
        double heartAttackProbabilityPerDT = heartAttackProbabilityPerSec * simulationTimeChange;
        double rand = random.nextDouble();
//        System.out.println( "PerDay = " + heartAttackProbabilityPerDay + ", perSec=" + heartAttackProbabilityPerSec + ", perDT=" + heartAttackProbabilityPerDT + ", rand=" + rand );
        if ( rand < heartAttackProbabilityPerDT ) {
            addHeartAttack();
        }
    }

    private void handleStarving( double simulationTimeChange ) {
        /*
        * Model for starvation:
           If you drop below 2%/4% (men/women) fat, you can live for 2 months and then death occurs. If you go above this level, the clock resets.
        */
        if ( isStarving() ) {
            starvingTime += simulationTimeChange;
        }
        else {
            starvingTime = 0;
        }
        if ( getStarvingTimeDays() > 30 * 2 && isAlive() ) {
            setAlive( false );
        }
    }

    private void addHeartAttack() {
        heartAttacks++;
        setAlive( false );
    }

    public double getHeartAttackProbabilityPerDay() {
        return gender.getHeartAttackProbabilityPerDay( this );
    }

    public double getStarvingTimeDays() {
        return EatingAndExerciseUnits.secondsToDays( starvingTime );
    }

    public boolean isStarving() {
        return gender.isStarving( this );
    }

    public boolean isAlmostStarving() {
        return gender.isAlmostStarving( this );
    }

    public double getDeltaCaloriesGainedPerDay() {
        return getDailyCaloricIntake() - getDailyCaloricBurn();
    }

    public double getDailyCaloricBurn() {
        return bmr.getValue() + activity.getValue() + exercise.getValue();
    }

    public double getDailyCaloricIntake() {
        return lipids.getValue() + proteins.getValue() + carbs.getValue();
    }

    public double getHeartHealth() {
        return ( getHeartStrength() + getHeartNonStrain() ) / 2;
    }

    private double getHeartNonStrain() {
        return 1 - getHeartStrain();
    }

    public double getFatMassPercent() {
        return fatMassFraction.getValue() * 100;
    }

    public double getFatFreeMassPercent() {
        return 100 - getFatMassPercent();
    }

    public void setFatMassPercent( double value ) {
        boolean starving = isStarving();
        double heartAttackProbabilityPerDay = getHeartAttackProbabilityPerDay();

        fatMassFraction.setValue( gender.clampFatMassPercent( value ) / 100.0 );
        updateBMR();
        notifyFatPercentChanged();

        if ( heartAttackProbabilityPerDay != getHeartAttackProbabilityPerDay() ) {
            notifyHeartAttackProbabilityChanged();
        }

        if ( starving != isStarving() ) {
            notifyStarvingChanged();
        }
    }

    private void notifyStarvingChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).starvingChanged();
        }
    }

    private void notifyHeartAttackProbabilityChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).heartAttackProbabilityChanged();
        }
    }

    public void clearMassData() {
        mass.clear();
    }

    public String getCauseOfDeath() {
        if ( getStarvingTimeDays() >= 60 ) {
            return "starvation";
        }
        else if ( heartAttacks > 0 ) {
            return "heart attack";
        }
        else {
            return "no known cause of death";
        }
    }

    public double getNormativePercentFat() {

//        	First calculate LBM:
//
//	LBM_men = BMI_0 * height^2 / 1.15
//	LBM_women = BMI_0 * height^2 / 1.22

//
//	BMI_0 = 18.5 (very sedentary)
//		20.0 (sedentary)
//		22.5 (moderately active)
//		25.0 (active)


        double BMI_0 = activityObject.getBMI_0();
        double LBM = BMI_0 * getHeight() * getHeight() / getGender().getLBMScaleFactor();
//
//	%_fat = (weight - LBM)/weight

        double percentFat = ( getMass() - LBM ) / getMass() * 100;
        return percentFat;
    }

    public double getHeartStrength() {
        return heartStrength;
    }

    public double getHeartStrain() {
        return heartStrain;
    }

    public double getCaloriesExerciseAndActivityPerDay() {
        return exerciseItems.getTotalCalories() + getActivityCaloriesPerDay();
    }

    public void setLeanFraction( double fracLean ) {
        setFatMassPercent( 100 - fracLean * 100 );
    }

    public double getCaloriesExercisePerDay() {
        return exerciseItems.getTotalCalories();
    }

    public static class Gender {
        public static Gender MALE = new Gender( EatingAndExerciseResources.getString( "gender.male" ).toLowerCase(), 0, 100, 4, 25, 1.15, 4, 27, 4, 6, 25, 4.0, 1 / 4.0 );
        public static Gender FEMALE = new Gender( EatingAndExerciseResources.getString( "gender.female" ).toLowerCase(), 0, 100, 8, 32, 1.22, 6, 23, 9, 16, 31, 2.25, 1 / 2.25 );
        private String name;
        private double minFatMassPercent;
        private double maxFatMassPercent;
        private double starvingFatMassPercentThreshold;
        private double heartAttackFatMassPercentThreshold;
        public static double P0 = 1.0 / 100.0;
        private double LMBScaleFactor;
        private double almostStarvingUpperThreshold;
        private double stdBMI;
        private double stdPercentFat;
        private double minPercentFatRange;
        private double maxPercentFatRange;
        private double fatMassMultiplier;
        private double fatMassLimiter;


        private Gender( String name, double minFatMassPercent, double maxFatMassPercent, double starvingFatMassPercentThreshold, double heartAttackFatMassPercentThreshold, double lmbScaleFactor, double almostStarvingUpperThreshold, double stdBMI, double stdPercentFat, double minPercentFatRange, double maxPercentFatRange, double fatMassMultiplier, double fatMassLimiter ) {
            this.name = name;
            this.minFatMassPercent = minFatMassPercent;
            this.maxFatMassPercent = maxFatMassPercent;
            this.starvingFatMassPercentThreshold = starvingFatMassPercentThreshold;
            this.heartAttackFatMassPercentThreshold = heartAttackFatMassPercentThreshold;
            this.LMBScaleFactor = lmbScaleFactor;
            this.almostStarvingUpperThreshold = almostStarvingUpperThreshold;
            this.stdBMI = stdBMI;
            this.stdPercentFat = stdPercentFat;
            this.minPercentFatRange = minPercentFatRange;
            this.maxPercentFatRange = maxPercentFatRange;
            this.fatMassMultiplier = fatMassMultiplier;
            this.fatMassLimiter = fatMassLimiter;
        }

        public double getStdBMI() {
            return stdBMI;
        }

        public double getStdPercentFat() {
            return stdPercentFat;
        }

        public String toString() {
            return name;
        }

        public double getMinFatMassPercent() {
            return minFatMassPercent;
        }

        public double getMaxFatMassPercent() {
            return maxFatMassPercent;
        }

        public double clampFatMassPercent( double value ) {
            return MathUtil.clamp( minFatMassPercent, value, maxFatMassPercent );
        }

        public boolean isStarving( Human human ) {
            return human.getFatMassPercent() < starvingFatMassPercentThreshold;
        }

//  p_attack = p_0 * (%fat - %fat_0)
//	where p_0 is a constant we adjust to make heart attack fairly
//  likely (within a couple of years) for %fat > 50
//  %fat_0 = 25%/32% for men/women.

        //new model should depend on heart health, as defined in Human

        public double getHeartAttackProbabilityPerDay( Human human ) {
            double heartHealth = human.getHeartHealth();//1 is perfect health

            int MIN_PROB = 0;
            double MAX_PROB = 1E-3;
            Function.LinearFunction linearFunction = new Function.LinearFunction( 0, 0.35, MAX_PROB, MIN_PROB );
            double prob = linearFunction.evaluate( heartHealth );
            prob = MathUtil.clamp( MIN_PROB, prob, MAX_PROB );
//            System.out.println( "prob = " + prob );
            return prob;
        }

        public double getLBMScaleFactor() {
            return LMBScaleFactor;
        }

        public boolean isAlmostStarving( Human human ) {
            return human.getFatMassPercent() >= starvingFatMassPercentThreshold && human.getFatMassPercent() <= almostStarvingUpperThreshold;
        }

        public double getStdLeanMassFraction() {
            return 1 - stdPercentFat / 100.0;
        }

        public double getDistanceFromNormalRangeInPercent( double fatMassPercent ) {
            if ( fatMassPercent < minPercentFatRange ) {
                return Math.abs( fatMassPercent - minPercentFatRange );
            }
            else if ( fatMassPercent > maxPercentFatRange ) {
                return Math.abs( fatMassPercent - maxPercentFatRange );
            }
            else {
                return 0;
            }
        }

        public double getFatMassMultiplier() {
            return fatMassMultiplier;
        }

        public double getFatMassLimiter() {
            return fatMassLimiter;
        }
    }

    public double getAge() {
        return age.getValue();
    }

    public void setAge( double age ) {
        if ( getAge() != age ) {
            this.age.setValue( age );
            notifyAgeChanged();
        }
    }

    private void notifyAgeChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            Listener listener = (Listener) listeners.get( i );
            listener.ageChanged();
        }
    }

    public double getHeight() {
        return height.getValue();
    }

    public void setHeight( double height ) {
        if ( this.height.getValue() != height ) {
            this.height.setValue( height );
            notifyHeightChanged();
            notifyBMIChanged();
        }
    }

    private void notifyBMIChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            Listener listener = (Listener) listeners.get( i );
            listener.bmiChanged();
        }
    }

    private void notifyHeightChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            Listener listener = (Listener) listeners.get( i );
            listener.heightChanged();
        }
    }

    public double getMass() {
        return mass.getValue();
    }

    public void setMass( double weight ) {
        double originalBmr = getBmr().getValue();
        this.mass.setValue( Math.max( weight, 0 ) );
        setFatMassPercent( 100 - ( ( originalBmr - 392 ) / 21.8 ) * ( 100 / weight ) );
        notifyFatPercentChanged();
        notifyWeightChanged();
        notifyBMIChanged();
    }

    private void notifyWeightChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            Listener listener = (Listener) listeners.get( i );
            listener.weightChanged();
        }
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender( Gender gender ) {
        if ( this.gender != gender ) {
            this.gender = gender;
            setFatMassPercent( gender.getMinFatMassPercent() );
            setHeight( getReferenceHuman( gender ).getHeightMeters() );
            setMass( getReferenceHuman( gender ).getMassKG() );
            notifyGenderChanged();
        }
    }

    private ReferenceHuman getReferenceHuman( Gender gender ) {
        if ( gender == Gender.MALE ) {
            return REFERENCE_MALE;
        }
        else {
            return REFERENCE_FEMALE;
        }
    }

    private void notifyGenderChanged() {
        for ( int i = 0; i < listeners.size(); i++ ) {
            ( (Listener) listeners.get( i ) ).genderChanged();
        }
    }

    public static interface Listener {
        void bmiChanged();

        void heightChanged();

        void weightChanged();

        void genderChanged();

        void musclePercentChanged();

        void fatPercentChanged();

        void ageChanged();

        void dietChanged();

        void exerciseChanged();

        void activityChanged();

        void caloricIntakeChanged();

        void caloricBurnChanged();

        void aliveChanged();

        void heartAttackProbabilityChanged();

        void starvingChanged();

        void activityLevelChanged();

        void heartStrainChanged();

        void heartStrengthChanged();

        void heartHealthChanged();
    }

    public static class Adapter implements Listener {

        public void bmiChanged() {
        }

        public void heightChanged() {
        }

        public void weightChanged() {
        }

        public void genderChanged() {
        }

        public void musclePercentChanged() {
        }

        public void fatPercentChanged() {
        }

        public void ageChanged() {
        }

        public void dietChanged() {
        }

        public void exerciseChanged() {
        }

        public void activityChanged() {
        }

        public void caloricIntakeChanged() {
        }

        public void caloricBurnChanged() {
        }

        public void aliveChanged() {
        }

        public void heartAttackProbabilityChanged() {
        }

        public void starvingChanged() {
        }

        public void activityLevelChanged() {
        }

        public void heartStrainChanged() {
        }

        public void heartStrengthChanged() {
        }

        public void heartHealthChanged() {
        }
    }


    public void addListener( Listener listener ) {
        listeners.add( listener );
    }

//    public void addFoodItem( FoodItem foodItem ) {
//
//        HashSet orig = new HashSet( foods );
//        foods.add( foodItem );
//        if ( !orig.equals( foods ) ) {
//            System.out.println( "added foodItem = " + foodItem );
//            notifyFoodItemsChanged();
//        }
//    }
//
//    private void notifyFoodItemsChanged() {
//        for ( int i = 0; i < listeners.size(); i++ ) {
//            Listener listener = (Listener) listeners.get( i );
//            listener.foodItemsChanged();
//        }
//    }
//
//    public void removeFoodItem( FoodItem foodItem ) {
//
//        HashSet orig = new HashSet( foods );
//        foods.remove( foodItem );
//        if ( !orig.equals( foods ) ) {
//            System.out.println( "removed foodItem = " + foodItem );
//            notifyFoodItemsChanged();
//        }

    //    }

    public static class ReferenceHuman {
        boolean male;
        double ageYears;
        double heightFT;
        double massKG;
        double fatFreeMassPercent;

        public ReferenceHuman( boolean male, double ageYears, double heightFT, double massKG, double fatFreeMassPercent ) {
            this.male = male;
            this.ageYears = ageYears;
            this.heightFT = heightFT;
            this.massKG = massKG;
            this.fatFreeMassPercent = fatFreeMassPercent;
        }

        public double getHeightMeters() {
            return EatingAndExerciseUnits.feetToMeters( heightFT );
        }

        public double getAgeSeconds() {
            return EatingAndExerciseUnits.yearsToSeconds( ageYears );
        }

        public double getMassKG() {
            return massKG;
        }

        public double getFatFreeMassPercent() {
            return fatFreeMassPercent;
        }

        public Gender getGender() {
            return male ? Gender.MALE : Gender.FEMALE;
        }
    }
}
