import {useState, useEffect, useCallback} from "react";

const API = "http://localhost:8080/api/v1";

function Stars({ rating }) {
  const full = Math.floor(rating || 0);
  const half = (rating || 0) % 1 >= 0.5;
  return (
    <span className="stars">
      {[...Array(5)].map((_, i) =>
        i < full ? "⬤" : i === full && half ? "◐" : "〇"
      )}
    </span>
  );
}

function Badge({ text }) {
  if (!text) return null;
  return <span className="badge">{text}</span>;
}

function PriceRange({ low, high }) {
  if (!low && !high) return <span className="price-empty">—</span>;
  if (!high || low === high) return <span className="price-value">{low} zł</span>;
  return <span className="price-value">{low}–{high} zł</span>;
}

function SalonCard({ salon, active, onClick }) {
  return (
      <div
          onClick={() => onClick(salon)}
          className={`salon-card${active ? " salon-card--active" : ""}`}
      >
        <div className="salon-card__header">
          <div className="salon-card__name-wrap">
            <p className="salon-card__name">{salon.nameOfBusiness}</p>
            <Badge text={salon.district} />
          </div>
          {active && <span className="salon-card__arrow">›</span>}
        </div>

        <div className="salon-card__footer">
          <div className="salon-card__rating">
            <Stars rating={salon.rating} />
            <span className="salon-card__rating-text">
            {salon.rating?.toFixed(1) || "—"} ({salon.amountOfReviews || 0})
          </span>
          </div>
          <PriceRange low={salon.priceLow} high={salon.priceHigh} />
        </div>
      </div>
  );
}

function FieldGroup({ label, children }) {
  return (
    <div className="field-group">
      <p className="field-group__label">{label}</p>
      <div className="field-group__content">{children}</div>
    </div>
  );
}

function DetailView({ salon, onEdit, onClose }) {
  return (
    <div>
      <div className="detail-header">
        <div>
          <div className="section-header">
            <span className="dot" />
            <p className="section-label">SALON DETAILS</p>
          </div>
          <h2 className="detail-title">{salon.nameOfBusiness}</h2>
        </div>
        <div style={{ display: "flex", gap: 8 }}>
          <button onClick={onEdit} className="btn-edit">Edit</button>
          <button onClick={onClose} className="btn-edit">✕</button>
        </div>
      </div>

      <div className="stats-box">
        <div className="stats-grid">
          <div>
            <p className="stat-label">Rating</p>
            <div className="stat-rating">
              <Stars rating={salon.rating} />
              <span className="stat-value">{salon.rating?.toFixed(1) || "—"}</span>
            </div>
          </div>
          <div>
            <p className="stat-label">Reviews</p>
            <p className="stat-value">{salon.amountOfReviews || "—"}</p>
          </div>
          <div>
            <p className="stat-label">Price range</p>
            <p className="stat-value">
              <PriceRange low={salon.priceLow} high={salon.priceHigh} />
            </p>
          </div>
        </div>
      </div>

      <div className="fields-grid">
        <FieldGroup label="District"><Badge text={salon.district} /></FieldGroup>
        <FieldGroup label="Address">
          <span className="address-text">{salon.address || "—"}</span>
        </FieldGroup>
        <FieldGroup label="Phone">
          {salon.phone
            ? <a href={`tel:${salon.phone}`} className="link">{salon.phone}</a>
            : <span className="text-muted">—</span>}
        </FieldGroup>
        <FieldGroup label="Website">
          {salon.website
            ? <a href={salon.website} target="_blank" rel="noreferrer" className="website-link">
                {salon.website.replace(/^https?:\/\//, "")}
              </a>
            : <span className="text-muted">—</span>}
        </FieldGroup>
      </div>

      <FieldGroup label="Services">
        {salon.services && salon.services.length > 0
          ? <div className="services-list">
              {salon.services.map((s, i) => <Badge key={i} text={s} />)}
            </div>
          : <span className="text-muted">—</span>}
      </FieldGroup>
    </div>
  );
}

function Field({ k, label, type = "text", form, onChange }) {
  return (
    <div className="field">
      <p className="field__label">{label}</p>
        <input
            type={type} step={type === "number" ? "0.1" : undefined}
            value={form[k] ?? ""}
            onChange={e => onChange(k, type === "number" ? parseFloat(e.target.value) || 0 : e.target.value)}
            className="input"
        />
    </div>
  );
}

function EditView({ form, onChange, onSave, onCancel, saving }) {
  return (
    <div>
      <div className="edit-header">
        <span className="dot" />
        <p className="section-label">EDITING</p>
        <span className="edit-name">— {form.nameOfBusiness}</span>
      </div>

      <div className="edit-grid">
        <div className="col-full"><Field k="nameOfBusiness" label="Business name" form={form} onChange={onChange} /></div>
        <div className="col-full"><Field k="address" label="Address" form={form} onChange={onChange} /></div>
        <Field k="district" label="District" form={form} onChange={onChange} />
        <Field k="phone" label="Phone" form={form} onChange={onChange} />
        <div className="col-full"><Field k="website" label="Website" form={form} onChange={onChange} /></div>
        <Field k="rating" label="Rating" type="number" form={form} onChange={onChange} />
        <Field k="amountOfReviews" label="Reviews" type="number" form={form} onChange={onChange} />
        <Field k="priceLow" label="Price low (zł)" type="number" form={form} onChange={onChange} />
        <Field k="priceHigh" label="Price high (zł)" type="number" form={form} onChange={onChange} />
        <div className="col-full field">
          <p className="services-label">Services (comma-separated)</p>
          <input
            type="text" className="input"
            value={(form.services || []).join(", ")}
            onChange={e => onChange("services", e.target.value.split(",").map(s => s.trim()).filter(Boolean))}
          />
        </div>
      </div>

      <div className="btn-group">
        <button onClick={onSave} disabled={saving} className="btn-save">
          {saving ? "Saving…" : "✓ Save changes"}
        </button>
        <button onClick={onCancel} disabled={saving} className="btn-cancel">
          Cancel
        </button>
      </div>
    </div>
  );
}

export default function App() {
  const [salons, setSalons] = useState([]);
  const [selected, setSelected] = useState(null);
  const [editing, setEditing] = useState(false);
  const [editForm, setEditForm] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);
  const [districtFilter, setDistrictFilter] = useState("");
  const [servicesFilter, setServicesFilter] = useState("");
  const [saveSuccess, setSaveSuccess] = useState(false);

  const fetchSalons = useCallback((district, services) => {
    const params = new URLSearchParams();
    if (district) params.set("district", district);
    if (services) {
      services.split(",")
          .map(s => s.trim())
          .filter(Boolean)
          .forEach(s => params.append("services", s));
    }

    fetch(`${API}/salons?${params}`)
        .then(r => { if (!r.ok) throw new Error(`HTTP ${r.status}`); return r.json(); })
        .then(data => { setSalons(data); setLoading(false); })
        .catch(e => { setError(e.message); setLoading(false); });
  }, []);

  useEffect(() => { fetchSalons("", ""); }, [fetchSalons]);

  const handleSearch = () => fetchSalons(districtFilter, servicesFilter);
  const handleClear = () => {
    setDistrictFilter("");
    setServicesFilter("");
    fetchSalons("", "");
  };

  const handleSelect = salon => { setSelected(salon); setEditing(false); setEditForm(null); setSaveSuccess(false); };
  const handleEdit = () => { setEditForm({ ...selected }); setEditing(true); };
  const handleCancel = () => { setEditing(false); setEditForm(null); };
  const handleChange = (key, val) => setEditForm(f => ({ ...f, [key]: val }));

  const handleSave = async () => {
    setSaving(true);
    try {
      const res = await fetch(`${API}/salons/${selected.id}/update`, {
        method: "PUT", headers: { "Content-Type": "application/json" },
        body: JSON.stringify(editForm),
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const updated = await res.json();
      setSelected(updated);
      setSalons(prev => prev.map(s => s.id === updated.id ? updated : s));
      setEditing(false); setEditForm(null); setSaveSuccess(true);
      setTimeout(() => setSaveSuccess(false), 3000);
    } catch (e) { alert("Save failed: " + e.message); }
    setSaving(false);
  };

  const showDetail = !!selected;

  return (
    <div className="app">

      {/* Header */}
      <div className="app-header">
        <div className="app-header__brand">
          <span className="dot dot--lg" />
          <span className="app-header__title">Warsaw Salon Beauty Explorer</span>
        </div>
        <div className="input-wrap">
          <input
              type="text" placeholder="District…"
              value={districtFilter}
              onChange={e => setDistrictFilter(e.target.value)}
              onKeyDown={e => e.key === "Enter" && handleSearch()}
              className="input input--search"
          />
          <input
              type="text" placeholder="Service…"
              value={servicesFilter}
              onChange={e => setServicesFilter(e.target.value)}
              onKeyDown={e => e.key === "Enter" && handleSearch()}
              className="input input--search"
          />
          <button onClick={handleSearch} className="btn-search">Search</button>
          {(districtFilter || servicesFilter) && (
              <button onClick={handleClear} className="btn-clear">✕</button>
          )}
        </div>
        <span className="app-header__count">{salons.length} salons</span>
      </div>

      {/* Body */}
      <div className="app-body">

        {/* List */}
        <div className={`list-panel ${showDetail ? "list-panel--sidebar" : "list-panel--full"}`}>
          {loading && (
            <div className="state-placeholder">
              <p className="state-text">Loading salons…</p>
            </div>
          )}
          {error && (
            <div className="error-box">
              <p className="error-title">Could not connect to backend</p>
              <p className="error-msg">{error}</p>
              <p className="error-note">
                Make sure Spring Boot is running on <code>localhost:8080</code> and CORS is enabled.
              </p>
            </div>
          )}
          {!loading && !error && salons.length === 0 && (
            <div className="state-placeholder">
              No salons match
              {districtFilter && <> district "<strong>{districtFilter}</strong>"</>}
              {districtFilter && servicesFilter && " and"}
              {servicesFilter && (
                  <> services "<strong>
                    {servicesFilter.split(",").map(s => s.trim()).filter(Boolean).join(", ")}
                  </strong>"</>
              )}
            </div>
          )}
          {salons.map(s => (
            <SalonCard key={s.id} salon={s} active={selected?.id === s.id} onClick={handleSelect} />
          ))}
        </div>

        {/* Detail / Edit panel */}
        {showDetail && (
          <div className="detail-panel">
            {saveSuccess && (
              <div className="success-toast">
                <span className="success-text">✓ Changes saved successfully</span>
              </div>
            )}
            {editing
              ? <EditView form={editForm} onChange={handleChange} onSave={handleSave} onCancel={handleCancel} saving={saving} />
              : <DetailView salon={selected} onEdit={handleEdit} onClose={() => setSelected(null)} />}
          </div>
        )}
      </div>
    </div>
  );
}
