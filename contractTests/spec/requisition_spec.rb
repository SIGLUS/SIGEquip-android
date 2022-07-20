describe "submit requisition to web server" do

  it "should sync requisitions to server successfully and return expected response" do

    #POST VIA requisition

    via_requisition =
    {
      agentCode: "HF2",
      programCode: "VIA",
      clientSubmittedTime: "2016-10-27 11:11:20",
      actualPeriodStartDate: "2016-01-20 11:11:20",
      actualPeriodEndDate: "2016-02-23 11:11:20",
      rnrSignatures: [
      {
        type: "SUBMITTER",
        text: "mystique"
      },
      {
        type: "APPROVER",
        text: "magneto"
      }
      ],
      products: [
      {
        productCode: "26B06",
        beginningBalance: 10,
        quantityReceived: 30,
        quantityDispensed: 20,
        stockInHand: 25,
        totalLossesAndAdjustments: 5,
        calculatedOrderQuantity: 15,
        quantityRequested: 20,
        quantityApproved: 15,
        reasonForRequestedQuantity: "reason"
      },
      {
        productCode: "26B07",
        beginningBalance: 100,
        quantityReceived: 300,
        quantityDispensed: 100,
        stockInHand: 300,
        totalLossesAndAdjustments: 0,
        calculatedOrderQuantity: 0,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason"
      }
      ] ,
      patientQuantifications: [
      {
        category: "consultation",
        total: 100
      }
      ]
    }

    via_response = RestClient.post "http://#{WEB_DEV_URI}/rest-api/requisitions",
      via_requisition.to_json, 'Content-Type' => 'application/json',
      'Accept' => 'application/json',
      'Authorization' => http_basic_auth('mystique', 'password1')

    expect(via_response.code).to eq 201

    body = JSON.parse(via_response.body)
    requisition_id = body['requisitionId']

    expect(requisition_id).not_to be_nil

    via_responseDuplicate = RestClient.post "http://#{WEB_DEV_URI}/rest-api/requisitions",
          via_requisition.to_json, 'Content-Type' => 'application/json',
          'Accept' => 'application/json',
          'Authorization' => http_basic_auth('mystique', 'password1')

    expect(via_responseDuplicate.code).to eq 200


    #POST MMIA requisition

    mmia_requisition =
    {
      agentCode: "HF2",
      programCode: "MMIA",
      clientSubmittedNotes: "I don't know",
      clientSubmittedTime: "2016-10-27 11:20:20",
      actualPeriodStartDate: "2016-01-21 11:11:20",
      actualPeriodEndDate: "2016-02-25 11:11:20",
      rnrSignatures: [
      {
        type: "SUBMITTER",
        text: "raven"
      },
      {
        type: "APPROVER",
        text: "professor-x"
      }
      ],
      products: [
      {
        productCode: "08S42",
        beginningBalance: 30,
        quantityReceived: 10,
        quantityDispensed: 20,
        totalLossesAndAdjustments: 0,
        stockInHand: 20,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S18Y",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S40",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S36",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S32",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S18Z",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S39Z",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S21",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S01",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S22",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S13",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S15",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S34B",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S32Z",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S42B",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S40Z",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S39B",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S39Y",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S01ZZ",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S20",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S19",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S01B",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S23",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      },
      {
        productCode: "08S17",
        beginningBalance: 10,
        quantityReceived: 10,
        quantityDispensed: 10,
        totalLossesAndAdjustments: 0,
        stockInHand: 10,
        quantityRequested: 0,
        quantityApproved: 0,
        reasonForRequestedQuantity: "reason",
        expirationDate: "10/10/2016"
      }
      ],
      regimens: [
      {
        code: "001",
        name: "AZT+3TC+NVP",
        patientsOnTreatment: 8,
        categoryName: "Adults"
      },
      {
        code: "002",
        name: "TDF+3TC+EFV",
        patientsOnTreatment: 2,
        categoryName: "Adults"
      },
      {
        code: "003",
        name: "AZT+3TC+EFV",
        patientsOnTreatment: 3,
        categoryName: "Adults"
      },
      {
        code: "004",
        name: "d4T 30+3TC+NVP",
        patientsOnTreatment: 1,
        categoryName: "Adults"
      },
      {
        code: "005",
        name: "d4T 30+3TC+EFV",
        patientsOnTreatment: 1,
        categoryName: "Adults"
      },
      {
        code: "006",
        name: "AZT+3TC+LPV/r",
        patientsOnTreatment: 1,
        categoryName: "Adults"
      },
      {
        code: "007",
        name: "TDF+3TC+LPV/r",
        patientsOnTreatment: 1,
        categoryName: "Adults"
      },
      {
        code: "008",
        name: "ABC+3TC+LPV/r",
        patientsOnTreatment: 1,
        categoryName: "Adults"
      },
      {
        code: "009",
        name: "d4T+3TC+NVP(3DFC Baby)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "010",
        name: "d4T+3TC+LPV/r(2DFC Baby + LPV/r)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "011",
        name: "d4T+3TC+ABC(2DFC Baby + ABC)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "012",
        name: "d4T+3TC+EFV(2DFC Baby + EFV)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "013",
        name: "AZT60+3TC+NVP(3DFC)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "014",
        name: "AZT60+3TC+EFV(2DFC + EFV)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "015",
        name: "AZT60+3TC+ABC(2DFC + ABC)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "016",
        name: "AZT60+3TC+LPV/r(2DFC + LPV/r)",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "017",
        name: "ABC+3TC+LPV/r",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        code: "018",
        name: "ABC+3TC+EFZ",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        name: "NEW+TEST",
        patientsOnTreatment: 1,
        categoryName: "Paediatrics"
      },
      {
        name: "NEW1+TEST2",
        patientsOnTreatment: 2,
        categoryName: "Adults"
      }
      ],
      patientQuantifications: [
      {
        category: "New Patients",
        total: 5
      },
      {
        category: "Sustaining",
        total: 1
      },
      {
        category: "Alteration",
        total: 1
      },
      {
        category: "PTV",
        total: 1
      },
      {
        category: "PPE",
        total: 26
      },
      {
        category: "Total Month Dispense",
        total: 1
      },
      {
        category: "Total Patients",
        total: 30
      }
      ]
    }

    mmia_response = RestClient.post "http://#{WEB_DEV_URI}/rest-api/requisitions",
      mmia_requisition.to_json, 'Content-Type' => 'application/json',
      'Accept' => 'application/json',
      'Authorization' => http_basic_auth('mystique', 'password1')

    expect(mmia_response.code).to eq 201

    body = JSON.parse(mmia_response.body)
    requisition_id = body['requisitionId']

    expect(requisition_id).not_to be_nil

    #Retrieve all requisitions

    response = RestClient.get "http://#{WEB_DEV_URI}/rest-api/requisitions?facilityCode=HF2",
      'Content-Type' => 'application/json',
      'Accept' => 'application/json',
      'Authorization' => http_basic_auth('mystique', 'password1')

    expect(response.code).to eq 200

    body = JSON.parse(response.body)

    #Check VIA requisition fields

    via_requisition = body['requisitions'].detect { |r| r['programCode'] == 'VIA' && !r['emergency']}

    expect(via_requisition['products'].length).to eq 2
    expect(via_requisition['patientQuantifications'].length).to eq 1
    expect(via_requisition['periodStartDate']).not_to be_nil
    expect(via_requisition['clientSubmittedTime']).not_to be_nil
    expect(via_requisition['actualPeriodStartDate']).not_to be_nil
    expect(via_requisition['actualPeriodEndDate']).not_to be_nil

    expect(via_requisition['rnrSignatures'].length).to eq 2

    via_submitter_signature = via_requisition['rnrSignatures'].detect { |s| s['type'] == 'SUBMITTER'}
    expect(via_submitter_signature['text']).to eq 'mystique'
    via_approver_signature = via_requisition['rnrSignatures'].detect { |s| s['type'] == 'APPROVER'}
    expect(via_approver_signature['text']).to eq 'magneto'


    product1 = via_requisition['products'].detect { |p| p['productCode'] == '26B06' }
    expect(product1['beginningBalance']).to eq 10
    expect(product1['quantityReceived']).to eq 30
    expect(product1['quantityDispensed']).to eq 20
    expect(product1['stockInHand']).to eq 25
    expect(product1['totalLossesAndAdjustments']).to eq 5
    expect(product1['calculatedOrderQuantity']).to eq 15
    expect(product1['quantityRequested']).to eq 20
    expect(product1['quantityApproved']).to eq 15

    patient_consultation = via_requisition['patientQuantifications'].detect { |p| p['category'] == 'consultation'}
    expect(patient_consultation['total']).to eq 100

    #Check MMIA requisition fields

    mmia_requisition = body['requisitions'].detect { |r| r['programCode'] == 'MMIA'}

    expect(mmia_requisition['products'].length).to eq 24
    expect(mmia_requisition['clientSubmittedNotes']).to eq "I don't know"
    expect(mmia_requisition['regimens'].length).to eq 20
    regimen_adults = mmia_requisition['regimens'].detect { |r| r['name'] == 'AZT+3TC+NVP'}
    expect(regimen_adults['categoryName']).to eq "Adults"
    regimen_paediatrics = mmia_requisition['regimens'].detect { |r| r['name'] == 'ABC+3TC+EFZ'}
    expect(regimen_paediatrics['categoryName']).to eq "Paediatrics"
    expect(mmia_requisition['patientQuantifications'].length).to eq 7
    expect(mmia_requisition['periodStartDate']).not_to be_nil
    expect(mmia_requisition['clientSubmittedTime']).not_to be_nil
    expect(via_requisition['actualPeriodStartDate']).not_to be_nil
    expect(via_requisition['actualPeriodEndDate']).not_to be_nil

    expect(mmia_requisition['rnrSignatures'].length).to eq 2

    mmia_submitter_signature = mmia_requisition['rnrSignatures'].detect { |s| s['type'] == 'SUBMITTER'}
    expect(mmia_submitter_signature['text']).to eq 'raven'
    mmia_approver_signature = mmia_requisition['rnrSignatures'].detect { |s| s['type'] == 'APPROVER'}
    expect(mmia_approver_signature['text']).to eq 'professor-x'

    product1 = mmia_requisition['products'].detect { |p| p['productCode'] == '08S23' }
    expect(product1['beginningBalance']).to eq 10
    expect(product1['quantityReceived']).to eq 10
    expect(product1['quantityDispensed']).to eq 10
    expect(product1['stockInHand']).to eq 10
    expect(product1['totalLossesAndAdjustments']).to eq 0
    expect(product1['expirationDate']).to eq '10/10/2016'

    regimen1 = mmia_requisition['regimens'].detect { |r| r['code'] == '001'}
    expect(regimen1['patientsOnTreatment']).to eq 8

    patient_quantification1 = mmia_requisition['patientQuantifications'].detect { |p| p['category'] == 'New Patients'}
    expect(patient_quantification1['total']).to eq 5
  end
end