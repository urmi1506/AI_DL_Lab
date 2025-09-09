package Assignment_8;

import java.util.*;

public class BackwardChaining {
    
    static class Rule {
        String conclusion;
        List<String> premises;
        
        Rule(String conclusion, List<String> premises) {
            this.conclusion = conclusion;
            this.premises = premises;
        }
        
        @Override
        public String toString() {
            return String.join(" ∧ ", premises) + " → " + conclusion;
        }
    }
    
    static class KnowledgeBase {
        List<Rule> rules = new ArrayList<>();
        Set<String> facts = new HashSet<>();
        Set<String> inferred = new HashSet<>();
        
        void addRule(String conclusion, String... premises) {
            rules.add(new Rule(conclusion, Arrays.asList(premises)));
        }
        
        void addFact(String fact) {
            facts.add(fact);
            System.out.println("Added fact: " + fact);
        }
        
        boolean knows(String fact) {
            return facts.contains(fact);
        }
    }
    
    public static boolean backwardChain(KnowledgeBase kb, String query) {
        System.out.println("\nStarting Backward Chaining");
        System.out.println("Initial facts: " + kb.facts);
        System.out.println("Rules: ");
        for (Rule rule : kb.rules) {
            System.out.println("  " + rule);
        }
        System.out.println("Query: " + query);
        System.out.println();
        
        boolean changed;
        int iteration = 1;
        
        Stack<String> goalStack = new Stack<>();
        goalStack.push(query);
        Set<String> proven = new HashSet<>();
        Set<String> failed = new HashSet<>();
        
        do {
            changed = false;
            System.out.println("--- Iteration " + iteration + " ---");
            
            if (goalStack.isEmpty()) {
                break;
            }
            
            String currentGoal = goalStack.peek();
            System.out.println("Current goal: " + currentGoal);
            
            if (kb.knows(currentGoal)) {
                System.out.println("✓ Goal is known fact: " + currentGoal);
                proven.add(currentGoal);
                goalStack.pop();
                changed = true;
                iteration++;
                continue;
            }
            
            if (proven.contains(currentGoal)) {
                System.out.println("Goal already proven: " + currentGoal);
                goalStack.pop();
                changed = true;
                iteration++;
                continue;
            }
            
            if (failed.contains(currentGoal)) {
                System.out.println("Goal already failed: " + currentGoal);
                goalStack.pop();
                iteration++;
                continue;
            }
            
            boolean foundRule = false;
            for (Rule rule : kb.rules) {
                if (rule.conclusion.equals(currentGoal)) {
                    foundRule = true;
                    System.out.println("Trying rule: " + rule);
                    
                    boolean allPremisesKnown = true;
                    for (String premise : rule.premises) {
                        if (!kb.knows(premise) && !proven.contains(premise)) {
                            if (!goalStack.contains(premise)) {
                                goalStack.push(premise);
                                System.out.println("Added subgoal: " + premise);
                            }
                            allPremisesKnown = false;
                        }
                    }
                    
                    if (allPremisesKnown) {
                        System.out.println("✓ Proved: " + currentGoal);
                        proven.add(currentGoal);
                        kb.inferred.add(currentGoal);
                        goalStack.pop();
                        changed = true;
                        
                        if (currentGoal.equals(query)) {
                            System.out.println("Query proven: " + query);
                            return true;
                        }
                        break;
                    }
                }
            }
            
            if (!foundRule) {
                System.out.println("No rules to prove: " + currentGoal);
                failed.add(currentGoal);
                goalStack.pop();
            }
            
            iteration++;
            if (iteration > 15) {
                System.out.println("Stopping after 15 iterations (safety limit)");
                break;
            }
            
        } while (changed || !goalStack.isEmpty());
        
        System.out.println("No more goals to prove");
        return proven.contains(query);
    }
    
    public static void main(String[] args) {
        KnowledgeBase kb = new KnowledgeBase();
        
        System.out.println("=== ANIMAL IDENTIFICATION SYSTEM ===");
        
        kb.addRule("is_bird", "has_feathers", "lays_eggs");
        kb.addRule("can_fly", "is_bird", "not_penguin");
        kb.addRule("is_penguin", "is_bird", "swims", "cannot_fly");
        kb.addRule("not_penguin", "can_fly");
        kb.addRule("not_penguin", "does_not_swim");
        
        kb.addFact("has_feathers");
        kb.addFact("lays_eggs");
        kb.addFact("swims");
        kb.addFact("cannot_fly");
        
        String query = "is_penguin";
        boolean result = backwardChain(kb, query);
        
        System.out.println("\n=== FINAL CONCLUSION ===");
        System.out.println("All known facts: " + kb.facts);
        System.out.println("Inferred facts: " + kb.inferred);
        System.out.println("Can prove '" + query + "': " + result);
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("=== WEATHER PREDICTION EXAMPLE ===");
        
        KnowledgeBase weatherKB = new KnowledgeBase();
        
        weatherKB.addRule("take_umbrella", "is_raining");
        weatherKB.addRule("is_raining", "dark_clouds", "high_humidity");
        weatherKB.addRule("stay_inside", "thunderstorm");
        weatherKB.addRule("thunderstorm", "is_raining", "lightning");
        
        weatherKB.addFact("dark_clouds");
        weatherKB.addFact("high_humidity");
        weatherKB.addFact("lightning");
        
        boolean shouldTakeUmbrella = backwardChain(weatherKB, "take_umbrella");
        System.out.println("Should take umbrella: " + shouldTakeUmbrella);
    }
}