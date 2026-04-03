Detection Time Scaling Analysis (IFCT vs MFCT)

Overview

This project investigates how detection time scales with system size in a distributed search setting using a combination of:

Agent-based simulation (Java, MASON)
Statistical modeling (Python)
Interactive visualization (Tableau Cloud)

The study focuses on two key metrics:

1. IFCT (Initial First Contact Time)
2. MFCT (Median First Contact Time)

Using simulation data, we evaluate whether detection processes follow predictable scaling laws and how stochastic variability impacts model performance.

Problem Statement

Many large-scale real-world systems (e.g., biological immune systems or distributed search processes), rely on fast detection such as fraud detection, anomaly detection, and
distributed monitoring systems. This project studies how detection time changes as system size grows and whether it can be modeled reliably.

This project answers:
- How does detection time scale with system size under different scaling of searchers and targets?
- Does the theoretical scaling model fit observed simulation data?

Approach

1. Simulation (Java – MASON)
Built an agent-based model (ABM) using MASON

Key components:
- Simulated searchers (T cells) and targets (DCs)
- Implemented movement strategies:
	|_Brownian Motion (BM)
	|_Correlated Random Walk (CRW)
- Configurable system size across multiple scales
- Generated stochastic simulation data with multiple replicates

Output: Detection times across different system configurations

2. Data Processing (Python)
- Converted simulation outputs into structured datasets
- Flattened replicate data into tidy format
- Applied log transformations
- Prepared data for modeling and visualization
3. Model Fitting
Fitted the following generalized hypothesis (See the following paper for details https://www.nature.com/articles/s41598-025-28443-2):

First Contact Time ∝ M^a log(cM)

IFCT (DC ∝ M^0.5) ∝ M^-0.5 ln(cM)
IFCT (DC = Constant) ∝ M^0 ln(cM)
MFCT (DC ∝ M^0.5) ∝ M^0 ln(cM)
MFCT (DC = Constant) ∝ M^0.5 ln(cM)

Implemented using:
- scipy.optimize.curve_fit
- Confidence intervals via covariance matrix
- Prediction comparison against theoretical exponent

4. Model Evaluation

Evaluated performance using:

- RMSE (Root Mean Squared Error) in log-space
- R² (Goodness of fit)
- AIC / BIC (model selection)

The model achieved low error (~13–15%) for MFCT, but large errors (~3–4×) for IFCT in terms of RMSE, indicating that average system behavior is predictable while first-event dynamics are significantly more stochastic.

Because IFCT distributions are highly variable, model evaluation was reperformed on the median IFCT (not MFCT) at each mass to assess how well the scaling law captures the central tendency across system size. While replicate-level IFCT exhibits large variability, median-based evaluation demonstrates that the scaling law accurately captures the central tendency across system size, with only moderate deviation (~20%). This indicates that stochastic fluctuations dominate individual outcomes, but underlying scaling behavior remains consistent.

Case  Exponent  Match 		RMSE (factor)  R² AIC/BIC
IFCT  Constant  0.04 ≈ 0 	1.20× 		   0.87  good
IFCT  Half     -0.48 ≈ -0.5 1.23× 		   0.97  good
MFCT  Constant  0.50 = 0.5  1.01× 		  ~1.00 excellent
MFCT  Half      0.01 ≈ 0 	1.02× 		  ~1.00 excellent

MFCT is highly predictable and follows the scaling law almost perfectly, whereas first detection time (IFCT) exhibits moderate deviation, reflecting underlying stochastic dynamics.

Fitted exponents is also compared against theoretical predictions to evaluate whether the system follows the expected scaling law. Across all configurations, the estimated exponents closely matched theoretical values, indicating that the underlying scaling behavior is correctly captured, even in the presence of high variability.


5. Visualization (Tableau Cloud)
- Built an interactive dashboard using Tableau Cloud

Key features:
- Overlay of raw data points + fitted curves
- Log-log scaling visualization
- Interactive filters:
- walk_type (BM vs CRW)
- metric (IFCT vs MFCT)
- dc_scaling (Constant vs M^0.5)
- Clear comparison of strategies

Key Insights
- Detection time follows a scaling relationship with system size
- Resource scaling (DC ~ M^0.5) stabilizes detection time
- Model fits closely match simulation trends
- Variability in raw data highlights stochastic behavior

Tech Stack:
Programming
|_JAVA(MASON)
|_Python (NumPy, Pandas, SciPy)
Modeling
|_Nonlinear curve fitting (curve_fit)
Statistical evaluation
|_RMSE, R², AIC, BIC
Visualization
|_Tableau Cloud (interactive dashboard)
Project Structure
├── Simulation Model (LN_Avg_contact_time_WithoutUIOriginal_/src/ln_first_contact_time_withoutUIOriginal)
|   |──Agent.java
|	|──AgentsSetting.java 		#main
|	|──AgentsWithUI.java       
|	|──Coordinate3D.java
|	|──DendriticCell.java
|	|──LymphNodes.java
|	|──TCell.java
|	|──TCellCoordinate3D.java
|	|──TCell_org.java
|
├── data/
│   ├── df_all.csv              # Raw simulation data
│   ├── fitted_curves.csv       # Model predictions
│   └── model_parameters.csv    # Fitted parameters
│
├── notebooks/
│   └── Main_IFCT_MFCT.ipynb  # Full analysis pipeline and model fitting functions
│
├── dashboard/
│   └── tableau_link.txt        # Dashboard link
│
└── README.md

Key Data Science Contributions
|--Built an end-to-end pipeline:
|--Experiment design -> simulation -> data processing -> modeling -> visualization
|--Applied hypothesis-driven modeling to real simulation data
|--Designed interactive dashboards for stakeholder-friendly insights
|--Compared multiple system configurations using statistical metrics

How This Relates to Data Science
This project demonstrates:
|-- Data cleaning and transformation
|-- Feature engineering
|-- Model fitting and evaluation
|-- Hypothesis testing
|-- Data visualization and storytelling
|--End-to-end analytical workflow

Future Improvements
|-- Extend model fitting to BM case
|-- Add uncertainty quantification
|-- Automate pipeline using PySpark
|-- Deploy dashboard with scheduled data refresh

Author

Jannatul Ferdous
PhD in Computer Science (Computational Modeling & Data Science)





