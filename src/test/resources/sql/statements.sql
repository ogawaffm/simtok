CREATE OR REPLACE PACKAGE BODY demo_pkg AS

  -- Procedure: Increase salary with exception handling
  PROCEDURE increase_salary (p_emp_id NUMBER, p_percent NUMBER) IS
BEGIN
UPDATE employees
SET emp_salary = emp_salary * (1 + p_percent / 100)
WHERE emp_id = p_emp_id;

IF SQL%ROWCOUNT = 0 THEN
      RAISE_APPLICATION_ERROR(-20001, 'No employee found.');
END IF;

COMMIT;
EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END increase_salary;

  -- Function: Retrieve employee name
  FUNCTION get_employee_name (p_emp_id NUMBER) RETURN VARCHAR2 IS
    v_name VARCHAR2(100);
BEGIN
SELECT emp_name INTO v_name FROM employees WHERE emp_id = p_emp_id;
RETURN v_name;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN 'Unknown';
END get_employee_name;

  -- Pipelined Function: Return multiple records
  FUNCTION fetch_employees RETURN t_emp_table PIPELINED IS
    v_emp t_employee;
BEGIN
FOR rec IN cur_emp LOOP
      v_emp.emp_id := rec.emp_id;
      v_emp.emp_name := rec.emp_name;
      v_emp.emp_salary := rec.emp_salary;
PIPE ROW(v_emp);
END LOOP;
END fetch_employees;

  -- Dynamic SQL with EXECUTE IMMEDIATE
  PROCEDURE execute_dynamic_query (p_sql VARCHAR2) IS
BEGIN
EXECUTE IMMEDIATE p_sql;
EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Error in dynamic SQL: ' || SQLERRM);
END execute_dynamic_query;

  -- Exception handling
  PROCEDURE exception_demo IS
    v_dummy NUMBER;
BEGIN
SELECT COUNT(*) INTO v_dummy FROM non_existing_table;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
      DBMS_OUTPUT.PUT_LINE('Table does not exist.');
WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('General error: ' || SQLERRM);
END exception_demo;

  -- Autonomous transaction (committed independently of other transactions)
  PROCEDURE log_event (p_message VARCHAR2) IS
  PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
INSERT INTO log_table (log_id, log_message, log_time
