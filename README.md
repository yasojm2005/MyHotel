# MyHotel
Repository for manage MyHotelTest
1-Dentro de cada proyecto se encuentra un readme donde se dan las instrucciones para levantar cada proyecto
2-En cada uno de los proyectos se agrega una collection de postman para que se puedan hacer pruebas a las API, ya este collection cuanta con ejemplos de llamada a cada una de las API


3-A continuacion la sconsultas de la pregunta numero 2 que tambien se encuentran en el repositorio del proyecto numero 2

3.1Obtener cantidad de empleados dentro de los siguientes segmentos de
sueldo:
SEGMENTO A: menor a USD 3.500
SEGMENTO B: mayor o igual a USD 3.500 y menor que USD 8.000
SEGMENTO C: mayor o igual a USD 8.000.

```
SELECT 
  CASE 
    WHEN SALARY < 3500 THEN 'SEGMENTO A'
    WHEN SALARY >= 3500 AND SALARY < 8000 THEN 'SEGMENTO B'
    WHEN SALARY >= 8000 THEN 'SEGMENTO C'
  END AS segmento,
  COUNT(*) AS cantidad
FROM 
  employees
GROUP BY 
  CASE 
    WHEN SALARY < 3500 THEN 'SEGMENTO A'
    WHEN SALARY >= 3500 AND SALARY < 8000 THEN 'SEGMENTO B'
    WHEN SALARY >= 8000 THEN 'SEGMENTO C'
  END;
```
3.2Utilizando la tabla “departments” se requiere realizar la misma agrupación
de segmentos de sueldo, pero por departamento

```
SELECT d.DEPARTMENT_NAME as department,
  SUM(CASE WHEN e.SALARY < 3500 THEN 1 ELSE 0 END) AS segmento_a,
  SUM(CASE WHEN e.SALARY >= 3500 AND e.SALARY < 8000 THEN 1 ELSE 0 END) AS segmneto_b,
  SUM(CASE WHEN e.SALARY >= 8000 THEN 1 ELSE 0 END) AS segmento_c
FROM employees e
JOIN departments d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID
GROUP BY d.DEPARTMENT_NAME;
```
3.3Información del empleado con mayor sueldo de cada departamento.

```
SELECT d.DEPARTMENT_NAME, e.FIRST_NAME
FROM employees e
INNER JOIN departments d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID
WHERE (e.SALARY, e.DEPARTMENT_ID) IN 
    (SELECT MAX(SALARY), DEPARTMENT_ID 
     FROM employees 
     GROUP BY DEPARTMENT_ID)
ORDER BY d.DEPARTMENT_NAME ASC;
```
3.4  Información de los gerentes que hayan sido contratados hace más de 15
años.

```
SELECT e.FIRST_NAME, e.LAST_NAME, e.HIRE_DATE
FROM employees e
INNER JOIN departments d ON e.EMPLOYEE_ID = d.MANAGER_ID
WHERE e.JOB_ID = 'MANAGER' AND DATEDIFF(CURDATE(), e.HIRE_DATE) > 15*365;
```
3.5 Salario promedio de todos los departamentos que tengan más de 10
empleados.
```
SELECT AVG(e.SALARY) as PROMEDIO_SALARIO, d.DEPARTMENT_NAME
FROM employees e
INNER JOIN departments d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID
GROUP BY d.DEPARTMENT_NAME
HAVING COUNT(*) > 10;
```
3.6 Obtener la siguiente información agrupada por país: cantidad empleados,
salario promedio, salario más alto, salario más bajo, promedio años
antigüedad

```
SELECT 
    l.COUNTRY_ID AS country,
    COUNT(e.EMPLOYEE_ID) AS num_employees,
    AVG(e.SALARY) AS avg_salary,
    MAX(e.SALARY) AS max_salary,
    MIN(e.SALARY) AS min_salary,
    AVG(DATEDIFF(CURDATE(), e.HIRE_DATE) / 365) AS avg_years_of_service
FROM 
    employees e
    JOIN departments d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID
    JOIN locations l ON d.LOCATION_ID = l.LOCATION_ID
GROUP BY 
    l.COUNTRY_ID
HAVING 
    COUNT(e.EMPLOYEE_ID) > 10
```    