c = Calendar.getInstance()
c.set(0, 0, 0, 0, 0);
c.add(Calendar.MINUTE, timeUnits)
result = new java.text.SimpleDateFormat("HH:mm").format(c.getTime())