@STRESS_TEST
Feature: Initial inventory and VIA requisition

  Scenario: Initial inventory
    Given I try to log in with "initial_inventory" "password1"
    Then I wait up to 120 seconds for "Initial Inventory" to appear
    And I initialize "1264" products
    And I press "Complete"

    And I wait up to 300 seconds for "STOCK CARD OVERVIEW" to appear
    And I press "Stock Card Overview"
    And I wait up to 120 seconds for "Stock Overview" to appear
    And I press "Sort alphabetically: A to Z"
    And I press "Sort by quantity: High to Low"
    And I wait for 10 seconds

    Then I can see stock on hand "1264" in position "1"
    Then I should see total:"1264" on stock list page

 # Scenario: Create VIA requisition
 #   And I change device date to "20160121.130000"
 #   Given I try to log in with "initial_inventory" "password1"
 #   And I wait up to 120 seconds for "STOCK CARD OVERVIEW" to appear
 #   And I press "Requisitions"
 #   And I wait for "Requisitions" to appear
 #   Then I should see text containing "No Via Classica Requisition has been created."

 #   And I press "Complete Inventory"
 #   And I wait up to 120 seconds for "inventory" to appear

 #   Then I do physical inventory for all items

 #   Then I wait up to 300 seconds for "Requisitions" to appear
 #   Then I should see text containing "Create Via Classica Requisition"

 #   And I press "Create Via Classica Requisition"
 #   And I wait up to 120 seconds for "Requisition -" to appear
 #   Then I navigate back
 #   Then I wait to see "Are you sure you want to quit without saving your work?"
 #   Then I press "Yes"
 #   Then I wait for "Requisitions" to appear
