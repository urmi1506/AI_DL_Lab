package Assignment_7;

import java.util.*;

public class ForwardChaining {
    
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
    
    public static boolean forwardChain(KnowledgeBase kb, String query) {
        System.out.println("\nStarting Forward Chaining");
        System.out.println("Initial facts: " + kb.facts);
        System.out.println("Rules: ");
        for (Rule rule : kb.rules) {
            System.out.println("  " + rule);
        }
        System.out.println("Query: " + query);
        System.out.println();
        
        boolean changed;
        int iteration = 1;
        
        do {
            changed = false;
            System.out.println("--- Iteration " + iteration + " ---");
            
            for (Rule rule : kb.rules) {
                if (kb.knows(rule.conclusion)) {
                    continue;
                }
                
                boolean allPremisesKnown = true;
                for (String premise : rule.premises) {
                    if (!kb.knows(premise)) {
                        allPremisesKnown = false;
                        break;
                    }
                }
                
                if (allPremisesKnown) {
                    kb.facts.add(rule.conclusion);
                    kb.inferred.add(rule.conclusion);
                    changed = true;
                    
                    System.out.println("Inferred: " + rule.conclusion + " using rule: " + rule);
                    
                    if (rule.conclusion.equals(query)) {
                        System.out.println("Query proven: " + query);
                        return true;
                    }
                }
            }
            
            iteration++;
            if (iteration > 15) {
                System.out.println("Stopping after 15 iterations (safety limit)");
                break;
            }
            
        } while (changed);
        
        System.out.println("No more facts can be inferred");
        return kb.knows(query);
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
        boolean result = forwardChain(kb, query);
        
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
        
        boolean shouldTakeUmbrella = forwardChain(weatherKB, "take_umbrella");
        System.out.println("Should take umbrella: " + shouldTakeUmbrella);
    }
}
