Given(/^Server updates drug data/) do
  system("cd #{LMIS_MOZ_DIR} && bash ./data/functional_tests/regression/update_products.sh")
end

Given(/^Server updates stock_movements data/) do
  system("cd #{LMIS_MOZ_DIR} && bash ./data/functional_tests/regression/update_stock_movements.sh")
end

Then(/^I clean up server drug data which I updated/) do
  system("cd #{LMIS_MOZ_DIR} && bash ./data/functional_tests/rollback_updated_products.sh")
end

Given(/^server deactivates products 12D03 and 07L01/) do
  system("cd #{LMIS_MOZ_DIR} && bash ./data/functional_tests/regression/deactivate_products.sh")
end

When(/^server reactive products/) do
  system("cd #{LMIS_MOZ_DIR} && bash ./data/functional_tests/regression/reactivate_products_have_stock_movement.sh")
end

Given(/^server deactivates products has stock movement/) do
  system("cd #{LMIS_MOZ_DIR} && bash ./data/functional_tests/regression/deactivate_products_have_stock_movement.sh")
end
