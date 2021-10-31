package mathcomp.oletsky.reinforcementlearning.markovdecisionnewnov2021;

import mathcomp.oletsky.mathhelper.VectMatr;

public class LearningTester {
    public static void main(String[] args) {
        //Common features
        String[] actions = {"Stay", "Right", "Left"};
        final int KOL_STATES=11;

        //Features of environment
        double[] rewards = {-1., -2., -3., -4., -5., -6., -5., -4., -3., -2., 10.};

        if (rewards.length!=KOL_STATES) throw new RuntimeException(
            "Number of states are different for the agent and the environment!"
        );
        System.out.println("Rewards:");
        VectMatr.defaultOutputVector(rewards);
        final double STEPPROB = 0.9;
        final double STAYPROB = 0.9;
        System.out.println("*****************");
        var env = new LinearAlternativeEnv(
                STAYPROB,
                STEPPROB,
                rewards,
                actions
        );

        //Features of agent
        final double GAMMA=0.95;
        final double ALPHA=0.01;
        final double BETA=0.01;
        final double COEFF=1.;
        final int INIT_STATE=0;

        double[][] actionValues;
        double[] stateValues;


        //Initializing
        LearningAgent agent=new LearningAgent(
                actions,
                GAMMA,
                ALPHA,
                BETA,
                COEFF,
                KOL_STATES,
                INIT_STATE
        );

        //Main iterations
        final int KOL_ITER=10000;
        for (int i = 0; i < KOL_ITER; i++) {
            agent.makeFullAction(env);

        }
        System.out.println("Final situation:");
        displayCurrentSituation(agent);
        System.out.println("Count of visits:");
        int[] counts = agent.getCounts();
        for (int i = 0; i < counts.length; i++) {
            System.out.println(i+" - "+counts[i]);

        }

    }

    static void displayCurrentSituation(LearningAgent agent) {
        System.out.println("State: "+agent.getState());
        double[] stateValues=agent.getStateValues();
        System.out.println("Values of states:");
        VectMatr.defaultOutputVector(stateValues);

        double[][] actionValues=agent.getActionValues();
        System.out.println("Values of actions:");
        VectMatr.defaultOutputMatrix(actionValues);

    }

}
