package cmq.core.bootstrap.resource;

public abstract interface Filter
{
  public abstract boolean accepts(String paramString);
}

