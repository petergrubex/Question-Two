import java.util.*;

// Base class for all participants
class Participant {
    private final String name;
    private String id;
    private int swimmingTime;
    private int cyclingTime;
    private int runningTime;
    private int totalTime;

    public Participant(String name, String id) {
        this.name = name;
        this.id = id;
        // Generate unique ID if not provided
        if (id == null || id.isEmpty()) {
            this.id = UUID.randomUUID().toString().substring(0, 8);
        }
    }

    // Getters and setters with validation
    public String getName() { return name; }
    public String getId() { return id; }
    
    public void setSwimmingTime(int time) {
        if (time < 0) throw new IllegalArgumentException("Swimming time cannot be negative");
        this.swimmingTime = time;
    }
    
    public void setCyclingTime(int time) {
        if (time < 0) throw new IllegalArgumentException("Cycling time cannot be negative");
        this.cyclingTime = time;
    }
    
    public void setRunningTime(int time) {
        if (time < 0) throw new IllegalArgumentException("Running time cannot be negative");
        this.runningTime = time;
    }
    
    public int getSwimmingTime() { return swimmingTime; }
    public int getCyclingTime() { return cyclingTime; }
    public int getRunningTime() { return runningTime; }
    
    public int calculateTotalTime() {
        totalTime = swimmingTime + cyclingTime + runningTime;
        return totalTime;
    }
    
    public int getTotalTime() { return totalTime; }
    
    public void displayDetails() {
        System.out.println("\nParticipant Details:");
        System.out.println("Name: " + name);
        System.out.println("ID: " + id);
        System.out.println("Total Time: " + totalTime + " minutes");
    }
}

// Elite participant class with sponsor information
class EliteParticipant extends Participant {
    private String sponsorName;
    
    public EliteParticipant(String name, String id, String sponsorName) {
        super(name, id);
        this.sponsorName = sponsorName;
    }
    
    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }
    
    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Sponsor: " + sponsorName);
    }
}

// Beginner participant class with base implementation
class BeginnerParticipant extends Participant {
    public BeginnerParticipant(String name, String id) {
        super(name, id);
    }
}

// Main class to manage triathlon results
class TriathlonResults {
    private final List<Participant> participants;
    
    public TriathlonResults() {
        participants = new ArrayList<>();
    }
    
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
    
    public void calculateAllTotalTimes() {
        for (Participant participant : participants) {
            participant.calculateTotalTime();
        }
    }
    
    public void displayResults() {
        // Sort participants by total time
        List<Participant> sortedParticipants = new ArrayList<>(participants);
        sortedParticipants.sort(Comparator.comparingInt(Participant::getTotalTime));
        
        System.out.println("\nTriathlon Results (Sorted by Total Time):");
        System.out.println("----------------------------------------");
        
        // Handle tied participants
        Map<Integer, List<Participant>> tiedParticipants = new HashMap<>();
        
        // Group participants by total time
        for (Participant p : sortedParticipants) {
            tiedParticipants
                .computeIfAbsent(p.getTotalTime(), k -> new ArrayList<>())
                .add(p);
        }
        
        // Display results with proper ranking
        int rank = 1;
        int displayRank = 1;
        
        for (int time : tiedParticipants.keySet().stream().sorted().toList()) {
            List<Participant> tied = tiedParticipants.get(time);
            
            for (Participant p : tied) {
                System.out.printf("Rank %d: %s (Total Time: %d minutes)%n",
                    displayRank, p.getName(), p.getTotalTime());
                p.displayDetails();
            }
            
            rank += tied.size();
            displayRank = rank;
        }
        
        // Display fastest and second fastest
        if (!sortedParticipants.isEmpty()) {
            System.out.println("\nFastest Participant:");
            sortedParticipants.get(0).displayDetails();
            
            if (sortedParticipants.size() > 1) {
                System.out.println("\nSecond Fastest Participant:");
                sortedParticipants.get(1).displayDetails();
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            TriathlonResults triathlon = new TriathlonResults();
            
            // Create participants
            EliteParticipant alice = new EliteParticipant("Alice", "E001", "SportsCo");
            alice.setSwimmingTime(25);
            alice.setCyclingTime(40);
            alice.setRunningTime(20);
            
            BeginnerParticipant bob = new BeginnerParticipant("Bob", "B001");
            bob.setSwimmingTime(20);
            bob.setCyclingTime(35);
            bob.setRunningTime(25);
            
            EliteParticipant charlie = new EliteParticipant("Charlie", "E002", "FitGear");
            charlie.setSwimmingTime(30);
            charlie.setCyclingTime(50);
            charlie.setRunningTime(30);
            
            BeginnerParticipant diana = new BeginnerParticipant("Diana", "B002");
            diana.setSwimmingTime(28);
            diana.setCyclingTime(42);
            diana.setRunningTime(18);
            
            // Add participants to triathlon
            triathlon.addParticipant(alice);
            triathlon.addParticipant(bob);
            triathlon.addParticipant(charlie);
            triathlon.addParticipant(diana);
            
            // Calculate total times and display results
            triathlon.calculateAllTotalTimes();
            triathlon.displayResults();
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}